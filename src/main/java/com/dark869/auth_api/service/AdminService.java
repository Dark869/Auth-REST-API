package com.dark869.auth_api.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dark869.auth_api.repository.UserRepository;
import com.dark869.auth_api.dto.AdminAllUsersResponse;
import com.dark869.auth_api.dto.AdminUserUnlockResponse;
import com.dark869.auth_api.model.User;

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

    public AdminUserUnlockResponse unlockUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setLocked(false);
        user.setFailedAttempts(0);
        user.setLockTime(null);
        userRepository.save(user);
        return new AdminUserUnlockResponse("User unlocked successfully");
    }
}
