package com.katech.service.job.repository.impl;

import com.katech.service.common.utils.QueryBuilderUtils;
import com.katech.service.job.dto.JobRequestResponseDto;
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

public class JobRequestCustomRepositoryImpl implements JobRequestCustomRepository {
    private final DSLContext dslContext;
    private static final Field<String> STATUS_FIELD = DSL.field("sl.status", String.class);

    private static final String BASE_SQL = """
    SELECT
           jr.*,
           s.name AS service_name,
           c.name AS customer_name,
           COUNT(q.id) AS quote_count
     FROM
          job_requests jr
          INNER JOIN services s ON jr.service_id = s.id
          INNER JOIN customers c ON jr.customer_id = c.id
          LEFT JOIN quotes q ON jr.id = q.job_request_id
     GROUP BY
          jr.id, s.name, c.name
""";
    @Override
    public Page<JobRequestResponseDto> getJobRequests(String status, Pageable pageable){
        return new QueryBuilderUtils<>(dslContext, BASE_SQL, JobRequestResponseDto.class)
                .addEqualCondition(STATUS_FIELD, status)
                .buildPage(pageable, "jr.created_at DESC");
    }
}
