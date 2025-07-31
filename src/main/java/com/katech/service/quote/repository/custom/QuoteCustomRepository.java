package com.katech.service.quote.repository.custom;

import com.katech.service.quote.dto.QuoteResponseRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuoteCustomRepository {
    Page<QuoteResponseRecord> getQuotesByJobRequest(String jobRequestId, Pageable pageable);
}