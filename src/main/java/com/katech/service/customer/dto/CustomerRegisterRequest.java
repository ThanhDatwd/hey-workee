package com.katech.service.customer.dto;

import lombok.Data;

@Data
public class CustomerRegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password; // plain password
    private String avatarUrl;
    private Double locationLat;
    private Double locationLng;
    private String address;
}
