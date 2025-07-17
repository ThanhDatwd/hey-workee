package com.katech.service.customer.dto;

import lombok.Builder;

public record OtpDetails(String otp, Long expireIn) {
    @Builder
    public OtpDetails {}
}
