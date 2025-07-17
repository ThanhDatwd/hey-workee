package com.katech.service.booking.enums;

public enum JobBookingStatus {
    CONFIRMED,     // Đơn đã được xác nhận
    ON_THE_WAY,    // Worker đang trên đường tới
    IN_PROGRESS,   // Đang thực hiện công việc
    COMPLETED,     // Hoàn tất công việc
    CANCELED       // Đơn bị hủy
}