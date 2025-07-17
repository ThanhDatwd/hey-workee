package com.katech.service.booking.repository;

import com.katech.service.booking.entity.JobBooking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobBookingRepository extends JpaRepository<JobBooking, UUID> {
}
