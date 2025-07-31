package com.katech.service.quote.dto;

import com.katech.service.quote.enums.QuoteStatus;
import lombok.Data;

import java.util.UUID;

@Data
public class QuoteRequest {
    private UUID jobRequestId;
    private UUID workerId;
    private UUID customerId;
    private Integer price;
    private Integer estimatedFinishTime;
    private Integer warrantyDuration;
    private String message;
    private QuoteStatus status;
}
