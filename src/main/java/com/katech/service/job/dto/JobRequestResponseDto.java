package com.katech.service.job.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobRequestResponseDto {
    private UUID customerId;
    private String customerName;
    private UUID serviceId;
    private String serviceName;
    private String description;
    private Double locationLat;
    private Double locationLng;
    private String address;
    private Integer radius;
    private Integer quoteCount;
    private String status;
    private String createdAt;
    private String updatedAt;


}
