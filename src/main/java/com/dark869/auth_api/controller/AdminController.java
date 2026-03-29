package com.dark869.auth_api.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dark869.auth_api.dto.AdminAllUsersResponse;
import com.dark869.auth_api.dto.AdminUserUnlockResponse;
import com.dark869.auth_api.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin", description = "Admin related endpoints")
@RestController
@RequestMapping("/api/${api.version}/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @Operation(summary = "Get all users", description = "Returns a paginated list of all users in the system")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Invalid pagination parameters")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @GetMapping("/users")
    public ResponseEntity<Page<AdminAllUsersResponse>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(adminService.getAllUsers(page, size));
    }

    @Operation(summary = "Unlock user", description = "Unlocks a user account")
    @ApiResponse(responseCode = "200", description = "User unlocked successfully")
    @ApiResponse(responseCode = "404", description = "User not found")
    @ApiResponse(responseCode = "403", description = "Access denied")
    @PatchMapping("/users/{userId}/unlock")
    public ResponseEntity<AdminUserUnlockResponse> unlockUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(adminService.unlockUser(userId));
    }
}
