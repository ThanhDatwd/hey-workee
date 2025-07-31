package com.katech.service.quote.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteResponseRecord {
    // Quote fields
    private String id;
    private String jobRequestId;
    private String workerId;
    private String customerId;
    private Double price;
    private String message;
    private Long estimatedFinishTime;
    private Integer warrantyDuration;
    private LocalDateTime createdAt;

    // Worker flat fields (from JOIN)
    private String workerName;
    private String workerAvatar;
    private Double workerLocationLat;
    private Double workerLocationLng;
    private Double workerRating;
    private Integer workerJobsDone;
    private String workerAddress;
    private Boolean workerVerified;

    /**
     * Transform flat record to nested DTO
     */
    public QuoteResponseDto toQuoteResponseDto() {
        // Create Worker DTO - KHÔNG DÙNG QuoteResponseDto.WorkerDto
        QuoteResponseDto.WorkerDto worker = new QuoteResponseDto.WorkerDto(
                this.workerId,
                this.workerName,
                this.workerAvatar,
                this.workerLocationLat,
                this.workerLocationLng,
                this.workerRating,
                this.workerJobsDone,
                this.workerAddress,
                this.workerVerified
        );

        // Create Quote DTO with nested Worker
        QuoteResponseDto quote = new QuoteResponseDto();
        quote.setId(this.id);
        quote.setJobRequestId(this.jobRequestId);
        quote.setWorkerId(this.workerId);
        quote.setCustomerId(this.customerId);
        quote.setPrice(this.price);
        quote.setMessage(this.message);
        quote.setEstimatedFinishTime(this.estimatedFinishTime);
        quote.setWarrantyDuration(this.warrantyDuration);
        quote.setCreatedAt(this.createdAt);
        quote.setWorker(worker);

        return quote;
    }
}