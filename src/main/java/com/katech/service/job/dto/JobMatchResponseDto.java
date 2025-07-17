package com.katech.service.job.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMatchResponseDto {
    private UUID id;
    private UUID jobRequestId;
    private UUID customerId;
    private String description;
    private Double locationLat;
    private Double locationLng;
    private String address;
    private Integer radius;
    private String status;
    private LocalDateTime matchedAt;
    private String customerName;
}