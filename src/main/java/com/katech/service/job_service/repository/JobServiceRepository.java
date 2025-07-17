package com.katech.service.job_service.repository;

import com.katech.service.job_service.entity.JobService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobServiceRepository extends JpaRepository<JobService, UUID> {
    boolean existsByName(String name);
}