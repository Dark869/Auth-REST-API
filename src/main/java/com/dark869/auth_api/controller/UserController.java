package com.dark869.auth_api.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dark869.auth_api.dto.UserChangePasswordRequest;
import com.dark869.auth_api.dto.UserChangePasswordResponse;
import com.dark869.auth_api.dto.UserResponse;
import com.dark869.auth_api.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "User", description = "Endpoints for user information")
@RestController
@RequestMapping("/api/${api.version}/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get current user info", description = "Returns information about the currently authenticated user.")
    @ApiResponse(responseCode = "200", description = "User information retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@AuthenticationPrincipal UserDetails userDetails) {
        UserResponse response = userService.getUserInfo(userDetails);
        return new ResponseEntity<UserResponse>(response, HttpStatus.OK);
    }

    @Operation(summary = "Change user password", description = "Allows the authenticated user to change their password.")
    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @ApiResponse(responseCode = "400", description = "Bad Request - invalid input or current password incorrect")
    @ApiResponse(responseCode = "401", description = "Unauthorized - user not authenticated")
    @ApiResponse(responseCode = "403", description = "Forbidden - user account is locked or disabled")
    @PatchMapping("/change_password")
    public ResponseEntity<UserChangePasswordResponse> changePassword(@AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UserChangePasswordRequest request) {
        UserChangePasswordResponse response = userService.changePassword(userDetails, request);
        return new ResponseEntity<UserChangePasswordResponse>(response, HttpStatus.OK);
    }
}