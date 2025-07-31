package com.katech.service.worker.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "workers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Worker {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;


    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "rating_avg")
    private Float ratingAvg;

    @Column(name = "jobs_done")
    private Integer jobsDone;

    @Column(name = "location_lat", nullable = false)
    private Double locationLat;

    @Column(name = "location_lng", nullable = false)
    private Double locationLng;

    @Column(name = "address")
    private String address;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "service_ids", columnDefinition = "uuid[]")
    private List<UUID> serviceIds;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (ratingAvg == null) ratingAvg = 5f;
        if (jobsDone == null) jobsDone = 0;
    }
}
