package com.katech.service.worker.dto;

import lombok.Builder;

public record OtpDetails(String otp, Long expireIn) {
    @Builder
    public OtpDetails {}
}
