package com.katech.service.quote.repository.impl;

import com.katech.service.common.utils.QueryBuilderUtils;
import com.katech.service.quote.dto.QuoteResponseRecord;
import com.katech.service.quote.repository.custom.QuoteCustomRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QuoteCustomRepositoryImpl implements QuoteCustomRepository {
    private final DSLContext dslContext;
    private static final Field<String> REQUEST_ID_FIELD = DSL.field("q.job_request_id", String.class);

    private static final String BASE_SQL = """
        SELECT
             q.id,
             q.price,
             q.message,
             q.created_at,
             q.job_request_id,
             q.worker_id,
             q.customer_id,
             q.estimated_finish_time,
             q.warranty_duration,
             w.id AS worker_id,
             w.name AS worker_name,
             w.avatar_url AS worker_avatar,
             w.rating_avg AS worker_rating,
             w.jobs_done AS worker_jobs_done,
             w.location_lat AS worker_location_lat,
             w.location_lng AS worker_location_lng,
             w.address AS worker_address
         FROM quotes q
         JOIN workers w ON q.worker_id = w.id
    """;

    @Override
    public Page<QuoteResponseRecord> getQuotesByJobRequest(String jobRequestId, Pageable pageable) {
        return new QueryBuilderUtils<>(dslContext, BASE_SQL, QuoteResponseRecord.class)
                .addEqualCondition(REQUEST_ID_FIELD, jobRequestId)
                .buildPage(pageable, "q.created_at DESC");
    }
}