package com.katech.service.quote.service;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.job.dto.JobMatchResponseDto;
import com.katech.service.quote.dto.QuoteNotification;
import com.katech.service.quote.dto.QuoteRequest;
import com.katech.service.quote.dto.QuoteResponseDto;
import com.katech.service.quote.dto.QuoteResponseRecord;
import com.katech.service.quote.entity.Quote;
import com.katech.service.quote.enums.QuoteStatus;
import com.katech.service.quote.mapper.QuoteMapper;
import com.katech.service.quote.repository.QuoteRepository;
import com.katech.service.quote.repository.custom.QuoteCustomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final QuoteCustomRepository quoteCustomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    private QuoteMapper quoteMapper;

    public PageableDto<QuoteResponseDto> getQuotesByJobRequest(String jobRequestId, Pageable pageable) {
        // Get flat records from repository
        var pageResult = quoteCustomRepository.getQuotesByJobRequest(jobRequestId, pageable);

        // Transform flat records to nested DTOs using toQuoteResponseDto() method
        List<QuoteResponseDto> quoteDtos = pageResult.getContent().stream()
                .map(QuoteResponseRecord::toQuoteResponseDto)
                .toList();

        return PageableDto.<QuoteResponseDto>builder()
                .content(quoteDtos)
                .totalElements(pageResult.getTotalElements())
                .build();
    }

    public Quote submitQuote(QuoteRequest request) {
        Quote quote = Quote.builder()
                .jobRequestId(request.getJobRequestId())
                .workerId(request.getWorkerId())
//                .workerName(request.getWorkerName())
                .customerId(request.getCustomerId())
                .price(request.getPrice())
                .message(request.getMessage())
                .estimatedFinishTime(request.getEstimatedFinishTime())
                .warrantyDuration(request.getWarrantyDuration())
                .status(QuoteStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Quote savedQuote = quoteRepository.save(quote);

        QuoteNotification notification = QuoteNotification.builder()
                .jobRequestId(savedQuote.getJobRequestId())
                .workerId(savedQuote.getWorkerId())
                .price(savedQuote.getPrice())
                .message(savedQuote.getMessage())
                .createdAt(savedQuote.getCreatedAt())
                .build();

        messagingTemplate.convertAndSend(
                "/topic/quote-notifications/" + savedQuote.getCustomerId(),
                notification
        );

        return savedQuote;
    }
    @Transactional
    public void acceptQuote(UUID quoteId) {
        Quote acceptedQuote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote không tồn tại"));

        acceptedQuote.setStatus(QuoteStatus.ACCEPTED);
        quoteRepository.save(acceptedQuote);

        // Reject các quote khác
        quoteRepository.rejectOthersInSameJob(acceptedQuote.getJobRequestId(), quoteId);

        // Gửi thông báo cho worker được nhận
        messagingTemplate.convertAndSend(
                "/topic/quote-status/" + acceptedQuote.getWorkerId(),
                QuoteNotification.builder()
                        .jobRequestId(acceptedQuote.getJobRequestId())
                        .workerId(acceptedQuote.getWorkerId())
                        .price(acceptedQuote.getPrice())
                        .message("Báo giá của bạn đã được khách hàng chấp nhận.")
                        .status(QuoteStatus.ACCEPTED)
                        .build()
        );

        // 🔁 Gửi thông báo đến các worker bị từ chối
        quoteRepository.findByJobRequestId(acceptedQuote.getJobRequestId()).stream()
                .filter(q -> !q.getId().equals(quoteId) && q.getStatus() == QuoteStatus.REJECTED)
                .forEach(rejectedQuote -> messagingTemplate.convertAndSend(
                        "/topic/quote-status/" + rejectedQuote.getWorkerId(),
                        QuoteNotification.builder()
                                .jobRequestId(rejectedQuote.getJobRequestId())
                                .workerId(rejectedQuote.getWorkerId())
                                .price(rejectedQuote.getPrice())
                                .message("Báo giá của bạn đã bị từ chối vì khách hàng đã chọn báo giá khác.")
                                .status(QuoteStatus.REJECTED)
                                .build()
                ));
    }


    @Transactional
    public void rejectQuote(UUID quoteId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote không tồn tại"));

        if (quote.getStatus() != QuoteStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể từ chối quote đang chờ");
        }

        quote.setStatus(QuoteStatus.REJECTED);
        quoteRepository.save(quote);

        // 🔔 Gửi WS cho worker báo giá bị từ chối
        QuoteNotification notification = QuoteNotification.builder()
                .jobRequestId(quote.getJobRequestId())
                .workerId(quote.getWorkerId())
                .price(quote.getPrice())
                .message("Báo giá của bạn đã bị từ chối.")
                .status(QuoteStatus.REJECTED)
                .build();

        messagingTemplate.convertAndSend(
                "/topic/quote-status/" + quote.getWorkerId(),
                notification
        );
    }


    @Transactional
    public void cancelQuoteByWorker(UUID quoteId, UUID workerId) {
        Quote quote = quoteRepository.findById(quoteId)
                .orElseThrow(() -> new RuntimeException("Quote không tồn tại"));

        if (!quote.getWorkerId().equals(workerId)) {
            throw new IllegalAccessError("Bạn không thể hủy báo giá của người khác");
        }

        if (quote.getStatus() != QuoteStatus.PENDING) {
            throw new IllegalStateException("Chỉ có thể hủy quote đang chờ");
        }

        quote.setStatus(QuoteStatus.CANCELED);
        quoteRepository.save(quote);

        // 🔔 Gửi WS cho customer biết quote bị huỷ
        QuoteNotification notification = QuoteNotification.builder()
                .jobRequestId(quote.getJobRequestId())
                .workerId(quote.getWorkerId())
                .price(quote.getPrice())
                .message("Người lao động đã huỷ báo giá.")
                .status(QuoteStatus.CANCELED)
                .build();

        messagingTemplate.convertAndSend(
                "/topic/quote-status/" + quote.getCustomerId(),
                notification
        );
    }
}
