package com.katech.service.booking.service;

import com.katech.service.booking.dto.BookingNotification;
import com.katech.service.booking.entity.CancelRequest;
import com.katech.service.booking.entity.JobBooking;
import com.katech.service.booking.enums.JobBookingStatus;
import com.katech.service.booking.repository.CancelRequestRepository;
import com.katech.service.booking.repository.JobBookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobBookingService {

    private final JobBookingRepository bookingRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final CancelRequestRepository cancelRequestRepository;

    private void notifyStatus(JobBooking booking, String message) {
        BookingNotification notification = BookingNotification.builder()
                .bookingId(booking.getId())
                .status(booking.getStatus())
                .message(message)
                .build();

        // Gửi cho cả customer và worker
        messagingTemplate.convertAndSend("/topic/booking-status/customer/" + booking.getCustomerId(), notification);
        messagingTemplate.convertAndSend("/topic/booking-status/worker/" + booking.getWorkerId(), notification);
    }
    private void notifyCancelRequest(UUID recipientId, String role, String message) {
        messagingTemplate.convertAndSend("/topic/booking-cancel-request/" + recipientId, role + " đã gửi yêu cầu huỷ: " + message);
    }


    @Transactional
    public JobBooking createBooking(UUID jobRequestId, UUID workerId, UUID customerId, Integer agreedPrice, LocalDateTime scheduledTime) {
        JobBooking booking = JobBooking.builder()
                .jobRequestId(jobRequestId)
                .workerId(workerId)
                .customerId(customerId)
                .agreedPrice(agreedPrice)
                .scheduledTime(scheduledTime)
                .status(JobBookingStatus.CONFIRMED)
                .createdAt(LocalDateTime.now())
                .build();

        JobBooking saved = bookingRepository.save(booking);
        notifyStatus(saved, "Đơn đã được xác nhận.");
        return saved;
    }

    @Transactional
    public void startOnTheWay(UUID bookingId, UUID workerId) {
        JobBooking booking = getBookingByIdAndCheckWorker(bookingId, workerId);
        booking.setStatus(JobBookingStatus.ON_THE_WAY);
        bookingRepository.save(booking);
        notifyStatus(booking, "Worker đang trên đường đến.");
    }

    @Transactional
    public void startWork(UUID bookingId, UUID workerId) {
        JobBooking booking = getBookingByIdAndCheckWorker(bookingId, workerId);
        booking.setStatus(JobBookingStatus.IN_PROGRESS);
        bookingRepository.save(booking);
        notifyStatus(booking, "Công việc đã bắt đầu.");
    }

    @Transactional
    public void completeBooking(UUID bookingId, UUID workerId) {
        JobBooking booking = getBookingByIdAndCheckWorker(bookingId, workerId);
        booking.setStatus(JobBookingStatus.COMPLETED);
        bookingRepository.save(booking);
        notifyStatus(booking, "Công việc đã hoàn tất.");
    }

    @Transactional
    public void cancelBooking(UUID bookingId, UUID userId, boolean isCustomer) {
        JobBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

        if (isCustomer && !booking.getCustomerId().equals(userId)) {
            throw new RuntimeException("Khách hàng không hợp lệ.");
        }

        if (!isCustomer && !booking.getWorkerId().equals(userId)) {
            throw new RuntimeException("Worker không hợp lệ.");
        }

        booking.setStatus(JobBookingStatus.CANCELED);
        bookingRepository.save(booking);
        notifyStatus(booking, "Đơn đã bị hủy.");
    }

    private JobBooking getBookingByIdAndCheckWorker(UUID bookingId, UUID workerId) {
        JobBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại"));

        if (!booking.getWorkerId().equals(workerId)) {
            throw new RuntimeException("Không có quyền.");
        }

        return booking;
    }


    // ------------------- Yêu cầu huỷ -------------------

    @Transactional
    public void requestCancelByCustomer(UUID bookingId, UUID customerId, String reason) {
        JobBooking booking = getBookingOrThrow(bookingId);

        if (!booking.getCustomerId().equals(customerId)) {
            throw new RuntimeException("Bạn không có quyền huỷ đơn này.");
        }

        // ✅ Huỷ trực tiếp nếu CONFIRMED
        if (booking.getStatus() == JobBookingStatus.CONFIRMED) {
            booking.setStatus(JobBookingStatus.CANCELED);
            bookingRepository.save(booking);
            notifyStatus(booking, "Bạn đã huỷ đơn thành công.");
            return;
        }

        // ❌ Nếu đã có yêu cầu huỷ thì không cho gửi tiếp
        if (cancelRequestRepository.existsByBookingId(bookingId)) {
            throw new RuntimeException("Đã có yêu cầu huỷ đang chờ xử lý cho đơn này.");
        }

        // ❌ Gửi yêu cầu huỷ nếu ON_THE_WAY
        if (booking.getStatus() == JobBookingStatus.ON_THE_WAY) {
            cancelRequestRepository.save(CancelRequest.builder()
                    .bookingId(bookingId)
                    .requesterId(customerId)
                    .requesterRole("CUSTOMER")
                    .workerId(booking.getWorkerId())
                    .customerId(customerId)
                    .reason(reason)
                    .createdAt(LocalDateTime.now())
                    .build()
            );

            notifyCancelRequest(booking.getWorkerId(), "Khách hàng", reason);
        } else {
            throw new IllegalStateException("Không thể huỷ đơn ở trạng thái hiện tại.");
        }
    }


    @Transactional
    public void requestCancelByWorker(UUID bookingId, UUID workerId, String reason) {
        JobBooking booking = getBookingOrThrow(bookingId);

        if (!booking.getWorkerId().equals(workerId)) {
            throw new RuntimeException("Bạn không có quyền gửi yêu cầu huỷ đơn.");
        }

        // ❌ Nếu đã có yêu cầu huỷ thì không cho gửi tiếp
        if (cancelRequestRepository.existsByBookingId(bookingId)) {
            throw new RuntimeException("Đã có yêu cầu huỷ đang chờ xử lý cho đơn này.");
        }

        // ❌ Gửi yêu cầu huỷ (không cho huỷ trực tiếp)
        if (booking.getStatus() == JobBookingStatus.CONFIRMED || booking.getStatus() == JobBookingStatus.ON_THE_WAY) {
            cancelRequestRepository.save(CancelRequest.builder()
                    .bookingId(bookingId)
                    .requesterId(workerId)
                    .requesterRole("WORKER")
                    .workerId(workerId)
                    .customerId(booking.getCustomerId())
                    .reason(reason)
                    .createdAt(LocalDateTime.now())
                    .build()
            );

            notifyCancelRequest(booking.getCustomerId(), "Worker", reason);
        } else {
            throw new IllegalStateException("Không thể huỷ đơn ở trạng thái hiện tại.");
        }
    }

    // ------------------- Xác nhận hoặc từ chối yêu cầu huỷ -------------------

    @Transactional
    public void confirmCancelRequest(UUID cancelRequestId, UUID confirmerId) {
        CancelRequest request = cancelRequestRepository.findById(cancelRequestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu huỷ không tồn tại."));

        JobBooking booking = getBookingOrThrow(request.getBookingId());

        boolean isAllowed = request.getRequesterRole().equals("CUSTOMER")
                ? booking.getWorkerId().equals(confirmerId)
                : booking.getCustomerId().equals(confirmerId);

        if (!isAllowed) {
            throw new RuntimeException("Bạn không có quyền xác nhận yêu cầu huỷ này.");
        }

        booking.setStatus(JobBookingStatus.CANCELED);
        bookingRepository.save(booking);

        notifyStatus(booking, "Đơn đã được huỷ theo yêu cầu.");

        cancelRequestRepository.deleteById(cancelRequestId);
    }

    @Transactional
    public void rejectCancelRequest(UUID cancelRequestId, UUID rejectorId) {
        CancelRequest request = cancelRequestRepository.findById(cancelRequestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu huỷ không tồn tại."));

        JobBooking booking = getBookingOrThrow(request.getBookingId());

        boolean isAllowed = request.getRequesterRole().equals("CUSTOMER")
                ? booking.getWorkerId().equals(rejectorId)
                : booking.getCustomerId().equals(rejectorId);

        if (!isAllowed) {
            throw new RuntimeException("Bạn không có quyền từ chối yêu cầu huỷ này.");
        }

        // Gửi WS từ chối huỷ cho requester
        messagingTemplate.convertAndSend(
                "/topic/booking-cancel-request/" + request.getRequesterId(),
                "Yêu cầu huỷ đơn của bạn đã bị từ chối."
        );

        cancelRequestRepository.deleteById(cancelRequestId);
    }

    // ------------------- Helper -------------------

    private JobBooking getBookingOrThrow(UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking không tồn tại."));
    }
}
