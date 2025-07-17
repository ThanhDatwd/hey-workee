package com.katech.service.booking.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class BookingRequest {
    private UUID jobRequestId;
    private UUID customerId;
    private UUID workerId;
    private Integer agreedPrice;
    private LocalDateTime scheduledTime;
}
