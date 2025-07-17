package com.katech.service.quote.repository;

import com.katech.service.quote.entity.Quote;
import com.katech.service.quote.enums.QuoteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, UUID> {
    List<Quote> findByJobRequestId(UUID jobRequestId);
    Optional<Quote> findByJobRequestIdAndWorkerId(UUID jobRequestId, UUID workerId);

    @Modifying
    @Query("UPDATE Quote q SET q.status = :status WHERE q.id = :id")
    void updateStatus(UUID id, QuoteStatus status);

    @Modifying
    @Query("UPDATE Quote q SET q.status = 'REJECTED' WHERE q.jobRequestId = :jobRequestId AND q.id <> :acceptedId")
    void rejectOthersInSameJob(UUID jobRequestId, UUID acceptedId);
}