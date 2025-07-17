package com.katech.service.job_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobServiceResponseDto {
    private UUID id;
    private String name;
    private String description;
    private String imageUrl;
    private Integer priceMin;
    private Integer priceMax;
    private boolean isActive;
}
