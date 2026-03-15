package com.dark869.auth_api.dto;

public record LoginResponse(
        String accessToken,
        String refreshToken) {
}
