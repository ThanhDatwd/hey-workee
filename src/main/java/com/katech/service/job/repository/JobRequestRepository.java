package com.katech.service.job.repository;

import com.katech.service.job.entity.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobRequestRepository extends JpaRepository<JobRequest, UUID> {
}
