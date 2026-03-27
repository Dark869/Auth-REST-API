package com.dark869.auth_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserChangePasswordRequest(
                @NotBlank(message = "Current password is required") String currentPassword,
                @NotBlank(message = "New password is required") @Size(min = 8, message = "Password should be at least 8 characters long") String newPassword) {
}
