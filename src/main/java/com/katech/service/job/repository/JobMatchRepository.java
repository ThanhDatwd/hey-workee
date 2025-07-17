package com.katech.service.job.repository;

import com.katech.service.job.entity.JobMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JobMatchRepository extends JpaRepository<JobMatch, UUID> {
}
