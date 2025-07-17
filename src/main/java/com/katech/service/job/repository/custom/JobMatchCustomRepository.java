package com.katech.service.job.repository.custom;

import com.katech.service.job.dto.JobMatchResponseDto;
import com.katech.service.job.dto.JobRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobMatchCustomRepository {
    Page<JobMatchResponseDto> getJobMatchesByWorker(String workerId, String status, Pageable pageable);
}
