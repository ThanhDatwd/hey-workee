package com.katech.service.customer.service;

import com.katech.service.customer.dto.CustomerRegisterRequest;
import com.katech.service.customer.entity.Customer;
import com.katech.service.customer.repository.CustomerRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(CustomerRegisterRequest request) {
        if (customerRepository.existsByEmail(request.getEmail())
                || customerRepository.existsByPhone(request.getPhone())) {
            throw new IllegalArgumentException("Email hoặc số điện thoại đã tồn tại.");
        }
        Customer customer =
                Customer.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .phone(request.getPhone())
                        .passwordHash(passwordEncoder.encode(request.getPassword()))
                        .avatarUrl(request.getAvatarUrl())
                        .locationLat(request.getLocationLat())
                        .locationLng(request.getLocationLng())
                        .address(request.getAddress())
                        .build();

        customerRepository.save(customer);
        return "Đăng ký người dùng thành công";
    }

    public Optional<Customer> getById(UUID id) {
        return customerRepository.findById(id);
    }
}
