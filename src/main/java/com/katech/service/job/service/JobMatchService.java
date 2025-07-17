package com.katech.service.job.service;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.job.dto.JobMatchResponseDto;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.job.repository.custom.JobMatchCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class JobMatchService {

    private  final JobMatchCustomRepository jobMatchCustomRepository;

    // GET LIST REQUEST MATCH WITH WORKER
    public PageableDto<JobMatchResponseDto> getJobMatchesByWorker(
            String workerId,
            String status,
            Pageable pageable
    ) {

        var pageExecutiveDocument =jobMatchCustomRepository.getJobMatchesByWorker(
                workerId,
                status,
                pageable
        );

        return PageableDto.<JobMatchResponseDto>builder()
                .content(pageExecutiveDocument.getContent())
                .totalElements(pageExecutiveDocument.getTotalElements())
                .build();

    }


}
