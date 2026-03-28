package com.dark869.auth_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dark869.auth_api.repository.UserRepository;
import com.dark869.auth_api.dto.AdminAllUsersResponse;

@Service
public class AdminService {
    private final UserRepository userRepository;

    public AdminService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Page<AdminAllUsersResponse> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable)
                .map(user -> new AdminAllUsersResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDisplayName(),
                        user.isEnabled(),
                        user.isLocked(),
                        user.getFailedAttempts(),
                        user.getCreatedAt()));
    }
}
