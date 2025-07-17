package com.katech.service.worker.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class WorkerRegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String avatarUrl;
    private String bio;
    private Double locationLat;
    private Double locationLng;
    private String address;
    private List<UUID> serviceIds;
}
