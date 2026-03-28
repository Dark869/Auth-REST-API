package com.dark869.auth_api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.dark869.auth_api.model.Role;

public record AdminAllUsersResponse(
        UUID id,
        String email,
        Role role,
        String displayName,
        boolean enabled,
        boolean locked,
        int failedAttempts,
        LocalDateTime createdAt) {
}