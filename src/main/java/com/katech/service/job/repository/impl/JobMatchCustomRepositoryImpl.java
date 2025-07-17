package com.katech.service.job.repository.impl;

import com.katech.service.common.utils.QueryBuilderUtils;
import com.katech.service.job.dto.JobMatchResponseDto;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.job.repository.custom.JobMatchCustomRepository;
import com.katech.service.job.repository.custom.JobRequestCustomRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor

public class JobMatchCustomRepositoryImpl implements JobMatchCustomRepository {
    private final DSLContext dslContext;
    private static final Field<String> STATUS_FIELD = DSL.field("jm.status", String.class);
    private static final Field<String> WORKER_ID_FIELD = DSL.field("jm.worker_id", String.class);

    private static final String BASE_SQL = """
    SELECT
        jm.*,
        jr.customer_id,
        jr.description,
        jr.location_lat,
        jr.location_lng,
        jr.address,
        jr.radius,
        jr.status,
        jr.created_at,
         c."name" AS customer_name
    FROM
        job_matches jm
        INNER JOIN job_requests jr ON jm.job_request_id = jr.id
        INNER JOIN customers c ON jr.customer_id = c.id
""";
    @Override
    public Page<JobMatchResponseDto> getJobMatchesByWorker(String workerId, String status, Pageable pageable){
        return new QueryBuilderUtils<>(dslContext, BASE_SQL, JobMatchResponseDto.class)
                .addEqualCondition(WORKER_ID_FIELD, workerId)
                .addEqualCondition(STATUS_FIELD, status)
                .buildPage(pageable, "jm.matched_at DESC");
    }
}
