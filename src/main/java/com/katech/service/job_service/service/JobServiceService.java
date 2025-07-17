package com.katech.service.job_service.service;

import com.katech.service.job_service.dto.JobServiceRequestDto;
import com.katech.service.job_service.dto.JobServiceResponseDto;
import com.katech.service.job_service.entity.JobService;
import com.katech.service.job_service.repository.JobServiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceService {

    private final JobServiceRepository jobServiceRepository;

    public List<JobServiceResponseDto> getAll() {
        return jobServiceRepository.findAll().stream()
                .map(job -> JobServiceResponseDto.builder()
                        .id(job.getId())
                        .name(job.getName())
                        .description(job.getDescription())
                        .imageUrl(job.getImageUrl())
                        .priceMin(job.getPriceMin())
                        .priceMax(job.getPriceMax())
                        .isActive(job.isActive())
                        .build())
                .collect(Collectors.toList());
    }

    public JobServiceRequestDto getById(UUID id) {
        JobService jobService = jobServiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ"));
        return toDto(jobService);
    }

    public JobServiceRequestDto create(JobServiceRequestDto dto) {
        JobService jobService = toEntity(dto);
        return toDto(jobServiceRepository.save(jobService));
    }

    private JobServiceRequestDto toDto(JobService jobService) {
        return JobServiceRequestDto.builder()
                .name(jobService.getName())
                .description(jobService.getDescription())
                .priceMin(jobService.getPriceMin())
                .priceMax(jobService.getPriceMax())
                .isActive(jobService.isActive())
                .build();
    }

    private JobService toEntity(JobServiceRequestDto dto) {
        return JobService.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .priceMin(dto.getPriceMin())
                .priceMax(dto.getPriceMax())
                .isActive(dto.isActive())
                .build();
    }
}
