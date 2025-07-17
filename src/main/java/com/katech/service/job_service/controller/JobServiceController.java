package com.katech.service.job_service.controller;

import com.katech.service.job_service.dto.JobServiceRequestDto;
import com.katech.service.job_service.dto.JobServiceResponseDto;
import com.katech.service.job_service.service.JobServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/job-services")
@RequiredArgsConstructor
public class JobServiceController {

    private final JobServiceService jobServiceService;

    @GetMapping
    public  List<JobServiceResponseDto> getAll() {
        return jobServiceService.getAll();
    }

    @GetMapping("/{id}")
    public JobServiceRequestDto getById(@PathVariable UUID id) {
        return jobServiceService.getById(id);
    }

    @PostMapping
    public JobServiceRequestDto create(@RequestBody JobServiceRequestDto request) {
        return jobServiceService.create(request);
    }
}
