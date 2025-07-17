package com.katech.service.job.repository.custom;

import com.katech.service.job.dto.JobRequestResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface JobRequestCustomRepository {
    Page<JobRequestResponseDto> getJobRequests( String status,Pageable pageable);
}
