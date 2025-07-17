package com.katech.service.booking.dto;

import com.katech.service.booking.enums.JobBookingStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BookingNotification {
    private UUID jobRequestId;
    private UUID bookingId;
    private JobBookingStatus status;
    private String message;
}
