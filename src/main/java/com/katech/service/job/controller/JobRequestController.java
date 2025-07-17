package com.katech.service.job.controller;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.customer.dto.BaseResponse;
import com.katech.service.job.dto.CreateJobRequestDto;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.job.entity.JobRequest;
import com.katech.service.job.service.JobRequestService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.katech.service.common.utils.ResponseBuilderUtils.buildSuccessResponse;

@RestController
@RequestMapping("/api/job-request")
@RequiredArgsConstructor
@Slf4j

public class JobRequestController {

    private final JobRequestService jobRequestService;

    @GetMapping
    public ResponseEntity<BaseResponse<PageableDto<JobRequestResponseDto>>> getJobRequests(
            @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "status", required = false) String status,
            HttpServletRequest request){

        return buildSuccessResponse( jobRequestService.getJobRequests(
                status,
                PageRequest.of(pageNumber, pageSize)
        ), request);

    }
    @PostMapping
    public ResponseEntity<JobRequest> createJob(@RequestBody CreateJobRequestDto jobRequest) {
        JobRequest savedJob = jobRequestService.createJobAndNotify(jobRequest);
        return ResponseEntity.ok(savedJob);
    }
}
