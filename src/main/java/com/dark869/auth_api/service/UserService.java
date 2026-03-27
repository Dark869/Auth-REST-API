package com.dark869.auth_api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dark869.auth_api.dto.UserChangePasswordRequest;
import com.dark869.auth_api.dto.UserChangePasswordResponse;
import com.dark869.auth_api.dto.UserResponse;
import com.dark869.auth_api.model.User;
import com.dark869.auth_api.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse getUserInfo(UserDetails userDetails) {
        User dataUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(dataUser.getEmail(), dataUser.getRole(), dataUser.getDisplayName());
    }

    public UserChangePasswordResponse changePassword(UserDetails userDetails, UserChangePasswordRequest request) {
        User dataUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (dataUser.isLocked()) {
            throw new IllegalStateException("User account is locked");
        }
        if (!passwordEncoder.matches(request.currentPassword(), dataUser.getPasswordHash())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        dataUser.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        userRepository.save(dataUser);
        return new UserChangePasswordResponse("Password changed successfully");
    }
}
