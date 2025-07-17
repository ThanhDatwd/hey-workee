package com.katech.service.quote.controller;

import com.katech.service.quote.dto.QuoteRequest;
import com.katech.service.quote.entity.Quote;
import com.katech.service.quote.service.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

//    @GetMapping("/job/{jobRequestId}")
//    public ResponseEntity<List<Quote>> getQuotesForJob(@PathVariable UUID jobRequestId) {
//        return ResponseEntity.ok(quoteService.getQuotesByJobRequest(jobRequestId));
//    }
}
