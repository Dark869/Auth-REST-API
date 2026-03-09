package com.dark869.auth_api.service;

import com.dark869.auth_api.dto.RegisterRequest;
import com.dark869.auth_api.dto.RegisterResponse;
import com.dark869.auth_api.model.User;
import com.dark869.auth_api.repository.UserRepository;
import com.dark869.auth_api.model.Role;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(false)
                .locked(false)
                .failedAttempts(0)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return new RegisterResponse(
                LocalDateTime.now(),
                201,
                "User registered successfully");
    }
}
