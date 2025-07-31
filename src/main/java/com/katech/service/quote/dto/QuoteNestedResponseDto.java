package com.katech.service.quote.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteNestedResponseDto {
    private String id;
    private String jobRequestId;
    private String workerId;
    private String customerId;
    private Double price;
    private String message;
    private Long estimatedFinishTime;
    private Integer warrantyDuration;
    private LocalDateTime createdAt;
    private WorkerDto worker;
}
