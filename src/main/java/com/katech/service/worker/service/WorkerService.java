package com.katech.service.worker.service;

import com.katech.service.worker.dto.WorkerRegisterRequest;
import com.katech.service.worker.entity.Worker;
import com.katech.service.worker.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerRepository workerRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(WorkerRegisterRequest request) {
        if (workerRepository.existsByEmail(request.getEmail())
                || workerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Email hoặc số điện thoại đã tồn tại.");
        }
        Worker worker = Worker.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .avatarUrl(request.getAvatarUrl())
                .bio(request.getBio())
                .locationLat(request.getLocationLat())
                .locationLng(request.getLocationLng())
                .address(request.getAddress())
                .serviceIds(request.getServiceIds())
                .build();

         workerRepository.save(worker);
         return "Tạo tài khoản thành công";
    }

    public Worker getById(String id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Worker not found"));
    }

    public List<Worker> getAll() {
        return workerRepository.findAll();
    }
}
