package com.katech.service.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestNotification {
    private UUID jobRequestId;
    private UUID customerId;
    private UUID serviceId;
    private String description;
    private String address;
    private double locationLat;
    private double locationLng;
    private int radius;
    private String status;
    private LocalDateTime createdAt;
}
