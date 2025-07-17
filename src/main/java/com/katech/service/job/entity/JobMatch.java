package com.katech.service.job.entity;

import com.katech.service.job.enums.JobStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "job_matches")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobMatch {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "job_request_id", nullable = false)
    private UUID jobRequestId;

    @Column(name = "worker_id", nullable = false)
    private UUID workerId;

    @Column(nullable = false)
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private JobStatus status = JobStatus.SENT;

    @Column(name = "matched_at", nullable = false)
    private LocalDateTime matchedAt;

    @PrePersist
    public void prePersist() {
        matchedAt = LocalDateTime.now();
    }
}
