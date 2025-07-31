package com.katech.service.quote.dto;

import com.katech.service.quote.enums.QuoteStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuoteNotification {
    private UUID jobRequestId;
    private UUID workerId;
    private String workerName;
    private Integer price;
    private String message;
    private QuoteStatus status;
    private Integer estimatedFinishTime;
    private LocalDateTime createdAt;
}
