package com.katech.service.job_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobServiceRequestDto {

    @NotBlank
    private String name;
    private String description;
    private String imageUrl;

    @NotNull
    private Integer priceMin;

    @NotNull
    private Integer priceMax;
    private boolean isActive;
}
