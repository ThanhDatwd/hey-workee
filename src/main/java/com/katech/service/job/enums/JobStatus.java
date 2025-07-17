package com.katech.service.job.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JobStatus {
    PENDING("PENDING"),
    SENT("SENT"),
    CONFIRMED("CONFIRMED"),
    COMPLETED("COMPLETED"),
    CANCELLED("CANCELLED");

    private final String value;

}