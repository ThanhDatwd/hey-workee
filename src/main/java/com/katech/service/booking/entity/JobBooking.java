package com.katech.service.booking.entity;

import com.katech.service.booking.enums.JobBookingStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobBooking {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private UUID jobRequestId;
    private UUID workerId;
    private UUID customerId;

    private Integer agreedPrice;

    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    private JobBookingStatus status = JobBookingStatus.CONFIRMED;

    private LocalDateTime createdAt = LocalDateTime.now();
}
