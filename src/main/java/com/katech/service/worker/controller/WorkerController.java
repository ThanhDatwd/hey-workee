package com.katech.service.worker.controller;

import com.katech.service.worker.dto.WorkerRegisterRequest;
import com.katech.service.worker.entity.Worker;
import com.katech.service.worker.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {

    private final WorkerService workerService;

    @PostMapping("/register")
    public String register(@RequestBody WorkerRegisterRequest request) {
        return workerService.register(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Worker> getById(@PathVariable String id) {
        return ResponseEntity.ok(workerService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<Worker>> getAll() {
        return ResponseEntity.ok(workerService.getAll());
    }
}
