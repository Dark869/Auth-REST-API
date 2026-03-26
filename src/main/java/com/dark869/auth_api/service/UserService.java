package com.dark869.auth_api.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.dark869.auth_api.dto.UserResponse;
import com.dark869.auth_api.model.User;
import com.dark869.auth_api.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public UserResponse getUserInfo(UserDetails userDetails) {
        User dataUser = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new UserResponse(dataUser.getEmail(), dataUser.getRole(), dataUser.getDisplayName());
    }
}
