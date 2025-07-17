package com.katech.service.booking.controller;

import com.katech.service.booking.entity.JobBooking;
import com.katech.service.booking.service.JobBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class JobBookingController {

    private final JobBookingService bookingService;

    /**
     * ✅ Tạo booking sau khi khách chọn quote
     */
    @PostMapping("/create")
    public ResponseEntity<JobBooking> createBooking(
            @RequestParam UUID jobRequestId,
            @RequestParam UUID workerId,
            @RequestParam UUID customerId,
            @RequestParam Integer agreedPrice,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime scheduledTime
    ) {
        JobBooking booking = bookingService.createBooking(jobRequestId, workerId, customerId, agreedPrice, scheduledTime);
        return ResponseEntity.ok(booking);
    }

    /**
     * 🛵 Worker báo đang trên đường đến
     */
    @PostMapping("/{bookingId}/worker/on-the-way")
    public ResponseEntity<Void> startOnTheWay(
            @PathVariable UUID bookingId,
            @RequestParam UUID workerId
    ) {
        bookingService.startOnTheWay(bookingId, workerId);
        return ResponseEntity.ok().build();
    }

    /**
     * 🔨 Worker bắt đầu làm việc
     */
    @PostMapping("/{bookingId}/worker/start")
    public ResponseEntity<Void> startWork(
            @PathVariable UUID bookingId,
            @RequestParam UUID workerId
    ) {
        bookingService.startWork(bookingId, workerId);
        return ResponseEntity.ok().build();
    }

    /**
     * ✅ Worker đánh dấu hoàn tất
     */
    @PostMapping("/{bookingId}/worker/complete")
    public ResponseEntity<Void> completeBooking(
            @PathVariable UUID bookingId,
            @RequestParam UUID workerId
    ) {
        bookingService.completeBooking(bookingId, workerId);
        return ResponseEntity.ok().build();
    }
    // ✅ Huỷ trực tiếp bởi customer nếu có quyền
    @PostMapping("/{bookingId}/customer/cancel")
    public ResponseEntity<Void> cancelByCustomer(@PathVariable UUID bookingId,
                                                 @RequestParam UUID customerId) {
        bookingService.requestCancelByCustomer(bookingId, customerId, null); // null lý do nếu huỷ trực tiếp
        return ResponseEntity.ok().build();
    }

    // ❌ Gửi yêu cầu huỷ bởi worker
    @PostMapping("/{bookingId}/worker/cancel-request")
    public ResponseEntity<Void> requestCancelByWorker(@PathVariable UUID bookingId,
                                                      @RequestParam UUID workerId,
                                                      @RequestParam(required = false) String reason) {
        bookingService.requestCancelByWorker(bookingId, workerId, reason);
        return ResponseEntity.ok().build();
    }

    // ❌ Gửi yêu cầu huỷ bởi customer (khi không được huỷ trực tiếp)
    @PostMapping("/{bookingId}/customer/cancel-request")
    public ResponseEntity<Void> requestCancelByCustomer(@PathVariable UUID bookingId,
                                                        @RequestParam UUID customerId,
                                                        @RequestParam(required = false) String reason) {
        bookingService.requestCancelByCustomer(bookingId, customerId, reason);
        return ResponseEntity.ok().build();
    }

    // ✅ Worker xác nhận yêu cầu huỷ
    @PostMapping("/cancel-request/{cancelRequestId}/worker/confirm")
    public ResponseEntity<Void> confirmCancelByWorker(@PathVariable UUID cancelRequestId,
                                                      @RequestParam UUID workerId) {
        bookingService.confirmCancelRequest(cancelRequestId, workerId);
        return ResponseEntity.ok().build();
    }

    // ✅ Customer xác nhận yêu cầu huỷ
    @PostMapping("/cancel-request/{cancelRequestId}/customer/confirm")
    public ResponseEntity<Void> confirmCancelByCustomer(@PathVariable UUID cancelRequestId,
                                                        @RequestParam UUID customerId) {
        bookingService.confirmCancelRequest(cancelRequestId, customerId);
        return ResponseEntity.ok().build();
    }

    // ❌ Worker từ chối huỷ
    @PostMapping("/cancel-request/{cancelRequestId}/worker/reject")
    public ResponseEntity<Void> rejectCancelByWorker(@PathVariable UUID cancelRequestId,
                                                     @RequestParam UUID workerId) {
        bookingService.rejectCancelRequest(cancelRequestId, workerId);
        return ResponseEntity.ok().build();
    }

    // ❌ Customer từ chối huỷ
    @PostMapping("/cancel-request/{cancelRequestId}/customer/reject")
    public ResponseEntity<Void> rejectCancelByCustomer(@PathVariable UUID cancelRequestId,
                                                       @RequestParam UUID customerId) {
        bookingService.rejectCancelRequest(cancelRequestId, customerId);
        return ResponseEntity.ok().build();
    }
}
