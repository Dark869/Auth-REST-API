package com.dark869.auth_api.dto;

import java.time.LocalDateTime;

public record RegisterResponse(
        LocalDateTime timestamp,
        int status,
        String message) {
}
