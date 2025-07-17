package com.katech.service.worker.repository;

import com.katech.service.worker.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.util.UUID;

public interface WorkerRepository extends JpaRepository<Worker, String> {
    Optional<Worker> findByEmail(String email);
    Optional<Worker> findByPhone(String phone);
    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    @Query(value = """
    SELECT w.* FROM workers w
    WHERE w.service_ids @> ARRAY[CAST(:serviceId AS uuid)]
    AND (
        6371000 * acos(
            cos(radians(:lat)) * cos(radians(w.location_lat)) *
            cos(radians(w.location_lng) - radians(:lng)) +
            sin(radians(:lat)) * sin(radians(w.location_lat))
        )
    ) <= :radius*1000
""", nativeQuery = true)
    List<Worker> findByLocationAndService(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("serviceId") UUID serviceId,
            @Param("radius") int radius
    );
}
