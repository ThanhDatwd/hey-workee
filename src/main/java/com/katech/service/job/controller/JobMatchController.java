package com.katech.service.job.controller;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.customer.dto.BaseResponse;
import com.katech.service.job.dto.CreateJobRequestDto;
import com.katech.service.job.dto.JobMatchResponseDto;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.job.entity.JobRequest;
import com.katech.service.job.service.JobMatchService;
import com.katech.service.job.service.JobRequestService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.katech.service.common.utils.ResponseBuilderUtils.buildSuccessResponse;

@RestController
@RequestMapping("/api/job-match")
@RequiredArgsConstructor
@Slf4j

public class JobMatchController {

    private final JobMatchService jobMatchService;

    @GetMapping("/worker/{workerId}")
    public ResponseEntity<BaseResponse<PageableDto<JobMatchResponseDto>>> getJobMatchesByWorker(
            @PathVariable String workerId,
            @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "status", required = false) String status,
            HttpServletRequest request){

        return buildSuccessResponse( jobMatchService.getJobMatchesByWorker(
                workerId,
                status,
                PageRequest.of(pageNumber, pageSize)
        ), request);

    }
}
