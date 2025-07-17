package com.katech.service.job.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateJobRequestDto {

    @NotNull(message = "customerId là bắt buộc")
    private UUID customerId;

//    @NotNull(message = "Tên khách hàng là bắt buộc")
//    private UUID customerName;

    @NotNull(message = "serviceId là bắt buộc")
    private UUID serviceId;

//    @NotNull(message = "Tên dịch vụ là bắt buộc")
//    private UUID serviceName;

    private String description;

    @NotNull(message = "Vĩ độ không được để trống")
    private Double locationLat;

    @NotNull(message = "Kinh độ không được để trống")
    private Double locationLng;

    @NotBlank(message = "Địa chỉ là bắt buộc")
    private String address;

    @Min(value = 1, message = "Bán kính tối thiểu là 1km")
    @Max(value = 100, message = "Bán kính tối đa là 100km")
    private Integer radius = 5; // mặc định nếu không truyền
}
