package com.dark869.auth_api.controller;

import com.dark869.auth_api.dto.LoginRequest;
import com.dark869.auth_api.dto.LoginResponse;
import com.dark869.auth_api.dto.LogoutRequest;
import com.dark869.auth_api.dto.LogoutResponse;
import com.dark869.auth_api.dto.RefreshRequest;
import com.dark869.auth_api.dto.RefreshResponse;
import com.dark869.auth_api.dto.RegisterRequest;
import com.dark869.auth_api.dto.RegisterResponse;
import com.dark869.auth_api.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for user authentication")
@RestController
@RequestMapping("/api/${api.version}/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account. Email must be unique.")
    @ApiResponse(responseCode = "201", description = "User registered successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "409", description = "Email already in use")
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = authService.register(request);
        return new ResponseEntity<RegisterResponse>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "User login", description = "Authenticates a user and returns access and refresh tokens.")
    @ApiResponse(responseCode = "200", description = "Login successful")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
    }

    @Operation(summary = "Refresh access token", description = "Generates a new access token using a valid refresh token.")
    @ApiResponse(responseCode = "200", description = "Token refreshed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Invalid or revoked refresh token")
    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshResponse response = authService.refresh(request);
        return new ResponseEntity<RefreshResponse>(response, HttpStatus.OK);
    }

    @Operation(summary = "User logout", description = "Revokes the provided refresh token, effectively logging the user out.")
    @ApiResponse(responseCode = "200", description = "Logged out successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @ApiResponse(responseCode = "401", description = "Invalid or revoked refresh token")
    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@Valid @RequestBody LogoutRequest request) {
        LogoutResponse response = authService.logout(request);
        return new ResponseEntity<LogoutResponse>(response, HttpStatus.OK);
    }

    @Operation(summary = "Logout from all sessions", description = "Revokes all refresh tokens for the authenticated user, logging them out from all devices.")
    @ApiResponse(responseCode = "200", description = "Logged out from all sessions successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @Transactional
    @PostMapping("/logout_all")
    public ResponseEntity<LogoutResponse> logoutAll(@AuthenticationPrincipal UserDetails userDetails) {
        LogoutResponse response = authService.logoutAll(userDetails);
        return new ResponseEntity<LogoutResponse>(response, HttpStatus.OK);
    }
}
