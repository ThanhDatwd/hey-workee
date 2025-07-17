package com.katech.service.booking.repository;

import com.katech.service.booking.entity.CancelRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CancelRequestRepository extends JpaRepository<CancelRequest, UUID> {
    boolean existsByBookingId(UUID bookingId);
    List<CancelRequest> findByCustomerId(UUID customerId);

    List<CancelRequest> findByWorkerId(UUID workerId);
}
