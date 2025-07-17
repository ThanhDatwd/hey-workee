package com.katech.service.quote.enums;

public enum QuoteStatus {
    PENDING,     // Đang chờ phản hồi từ khách
    ACCEPTED,    // Được khách chọn và tạo booking
    REJECTED,    // Bị loại vì khách chọn quote khác
    EXPIRED,
    CANCELED// Hết hạn phản hồi hoặc job đã bị hủy
}
