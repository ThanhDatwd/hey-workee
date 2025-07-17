package com.katech.service.quote.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class QuoteRequest {
    private UUID jobRequestId;
    private UUID workerId;
    private String workerName;
    private UUID customerId;
    private Integer price;
    private String message;
}
