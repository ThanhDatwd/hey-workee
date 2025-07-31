package com.katech.service.quote.entity;

import com.katech.service.quote.enums.QuoteStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "quotes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Quote {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private UUID jobRequestId;

    @Column(nullable = false)
    private UUID workerId;

    @Column(nullable = false)
    private UUID customerId;

    @Column(nullable = false)
    private Integer price;

    @Column(name="estimated_finish_time", nullable = true)
    private Integer estimatedFinishTime;

    @Column(name="warranty_duration", nullable = true)
    private Integer warrantyDuration;

    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuoteStatus status = QuoteStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
