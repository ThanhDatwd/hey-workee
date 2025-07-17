package com.katech.service.customer.controller;

import com.katech.service.customer.dto.CustomerRegisterRequest;
import com.katech.service.customer.service.CustomerService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public String register(@RequestBody CustomerRegisterRequest request) {
        return customerService.register(request);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(@PathVariable UUID id) {
//        return customerService
//                .getById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
}
