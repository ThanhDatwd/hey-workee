package com.katech.service.worker.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class WorkerResponseDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String avatarUrl;
    private String bio;
    private Double locationLat;
    private Double locationLng;
    private String address;
    private List<UUID> serviceIds;
    private Boolean verified;
}
