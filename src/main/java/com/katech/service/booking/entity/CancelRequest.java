package com.katech.service.booking.entity;

import com.katech.service.booking.enums.JobBookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "cancel_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelRequest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private UUID bookingId;
    private UUID requesterId; // Người gửi yêu cầu huỷ
    private String requesterRole; // "CUSTOMER" hoặc "WORKER"
    private String reason;

    private UUID workerId;
    private UUID customerId;

    private LocalDateTime createdAt = LocalDateTime.now();
}
