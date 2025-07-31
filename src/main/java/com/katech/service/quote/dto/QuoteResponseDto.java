// QuoteResponseDto.java - With nested WorkerDto
package com.katech.service.quote.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class QuoteResponseDto {
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

    // Nested WorkerDto class
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class WorkerDto {
        private String id;
        private String name;
        private String avatarUrl;
        private Double locationLat;
        private Double locationLng;
        private Double ratingAvg;
        private Integer jobsDone;
        private String address;
        private Boolean verified;
    }
}