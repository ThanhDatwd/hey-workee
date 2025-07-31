package com.katech.service.quote.controller;

import com.katech.service.common.dto.PageableDto;
import com.katech.service.customer.dto.BaseResponse;
import com.katech.service.job.dto.JobRequestResponseDto;
import com.katech.service.quote.dto.QuoteRequest;
import com.katech.service.quote.dto.QuoteResponseDto;
import com.katech.service.quote.entity.Quote;
import com.katech.service.quote.service.QuoteService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.katech.service.common.utils.ResponseBuilderUtils.buildSuccessResponse;

@RestController
@RequestMapping("/api/quotes")
@RequiredArgsConstructor
public class QuoteController {

    private final QuoteService quoteService;

    @PostMapping
    public ResponseEntity<Quote> submitQuote(@RequestBody QuoteRequest request) {
        return ResponseEntity.ok(quoteService.submitQuote(request));
    }

    @PutMapping("/{id}/accept")
    public ResponseEntity<Void> acceptQuote(@PathVariable UUID id) {
        quoteService.acceptQuote(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectQuote(@PathVariable UUID id) {
        quoteService.rejectQuote(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelQuoteByWorker(
            @PathVariable UUID id,
            @RequestParam UUID workerId
    ) {
        quoteService.cancelQuoteByWorker(id, workerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<BaseResponse<PageableDto<QuoteResponseDto>>> getQuotesByJobRequest(
            @RequestParam(value = "jobRequestId", required = false) String jobRequestId ,
            @RequestParam(value = "pageSize", defaultValue = "50") Integer pageSize,
            @RequestParam(value = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(value = "status", required = false) String status,
            HttpServletRequest request) {

        return buildSuccessResponse( quoteService.getQuotesByJobRequest(
                jobRequestId,
                PageRequest.of(pageNumber, pageSize)
        ), request);
    }
}
