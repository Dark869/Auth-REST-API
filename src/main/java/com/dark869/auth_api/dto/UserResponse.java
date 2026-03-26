package com.dark869.auth_api.dto;

import com.dark869.auth_api.model.Role;

public record UserResponse(
        String email,
        Role role,
        String displayName) {
}