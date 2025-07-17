package com.katech.service.job.service;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.job.dto.CreateJobRequestDto;
import com.katech.service.job.dto.JobRequestNotification;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.job.entity.JobMatch;
import com.katech.service.job.entity.JobRequest;
import com.katech.service.job.enums.JobStatus;
import com.katech.service.job.repository.JobMatchRepository;
import com.katech.service.job.repository.JobRequestRepository;
import com.katech.service.job.repository.custom.JobRequestCustomRepository;
import com.katech.service.worker.entity.Worker;
import com.katech.service.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor

public class JobRequestService {

    private final JobRequestRepository jobRequestRepository;
    private  final JobRequestCustomRepository jobRequestCustomRepository;
    private final WorkerRepository workerRepository;
    private final JobMatchRepository jobMatchRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public PageableDto<JobRequestResponseDto> getJobRequests(
            String status,
            Pageable pageable
    ) {

        var pageExecutiveDocument =jobRequestCustomRepository.getJobRequests(
                status,
                pageable
        );

        return PageableDto.<JobRequestResponseDto>builder()
                .content(pageExecutiveDocument.getContent())
                .totalElements(pageExecutiveDocument.getTotalElements())
                .build();

    }
    @Transactional
    public JobRequest createJobAndNotify(CreateJobRequestDto jobRequest) {
        // 1. Lưu job request
        JobRequest job = JobRequest.builder()
                .customerId(jobRequest.getCustomerId())
                .serviceId(jobRequest.getServiceId())
                .description(jobRequest.getDescription())
                .locationLat(jobRequest.getLocationLat())
                .locationLng(jobRequest.getLocationLng())
                .address(jobRequest.getAddress())
                .radius(jobRequest.getRadius() != null ? jobRequest.getRadius() : 5) // fallback nếu null
                .status(JobStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        // 2. Lưu job request
        JobRequest savedJob = jobRequestRepository.save(job);

        // 2. Tìm worker phù hợp
        List<Worker> matchedWorkers = workerRepository.findByLocationAndService(
                savedJob.getLocationLat(),
                savedJob.getLocationLng(),
                savedJob.getServiceId(),
                savedJob.getRadius()
        );

        // 3. Tạo job_match + gửi notification qua WebSocket
        for (Worker worker : matchedWorkers) {
            JobMatch jobMatch = JobMatch.builder()
                    .jobRequestId(savedJob.getId())
                    .workerId(worker.getId())
                    .status(JobStatus.SENT)
                    .matchedAt(LocalDateTime.now())
                    .build();

            jobMatchRepository.save(jobMatch);
            log.info("Job match id: {}", worker.getId());
            JobRequestNotification notification = JobRequestNotification.builder()
                    .jobRequestId(savedJob.getId())
                    .customerId(savedJob.getCustomerId())
                    .serviceId(savedJob.getServiceId())
                    .description(savedJob.getDescription())
                    .address(savedJob.getAddress())
                    .locationLat(savedJob.getLocationLat())
                    .locationLng(savedJob.getLocationLng())
                    .radius(savedJob.getRadius())
                    .status(savedJob.getStatus().name())
                    .createdAt(savedJob.getCreatedAt())
                    .build();

            messagingTemplate.convertAndSend(
                    "/topic/job-notifications/" + worker.getId(),
                    notification
            );
        }

        return savedJob;
    }

}
