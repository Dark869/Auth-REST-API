package com.dark869.auth_api.service;

import com.dark869.auth_api.dto.LoginRequest;
import com.dark869.auth_api.dto.LoginResponse;
import com.dark869.auth_api.dto.RefreshRequest;
import com.dark869.auth_api.dto.RefreshResponse;
import com.dark869.auth_api.dto.RegisterRequest;
import com.dark869.auth_api.dto.RegisterResponse;
import com.dark869.auth_api.exception.InvalidTokenException;
import com.dark869.auth_api.model.User;
import com.dark869.auth_api.model.RefreshToken;
import com.dark869.auth_api.model.Role;
import com.dark869.auth_api.repository.UserRepository;
import com.dark869.auth_api.repository.RefreshTokenRepository;
import com.dark869.auth_api.security.JwtService;
import com.dark869.auth_api.utils.HashUtil;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
            RefreshTokenRepository refreshTokenRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("Email already in use");
        }
        User user = User.builder()
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .enabled(false)
                .locked(false)
                .failedAttempts(0)
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(user);
        return new RegisterResponse(
                LocalDateTime.now(),
                201,
                "User registered successfully");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        String accessToken = jwtService.generateAccessToken(request.email());
        String refreshToken = UUID.randomUUID().toString();

        String hashedRefreshToken = HashUtil.sha256(refreshToken);

        RefreshToken tokenEntity = RefreshToken.builder()
                .tokenHash(hashedRefreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();
        refreshTokenRepository.save(tokenEntity);

        return new LoginResponse(
                accessToken,
                refreshToken);
    }

    public RefreshResponse refresh(RefreshRequest request) {
        String hashedToken = HashUtil.sha256(request.refreshToken());
        RefreshToken tokenEntity = refreshTokenRepository.findByTokenHash(hashedToken)
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        if (tokenEntity.isRevoked() || tokenEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new InvalidTokenException("Refresh token is revoked or expired");
        }
        User user = tokenEntity.getUser();

        String newAccessToken = jwtService.generateAccessToken(user.getEmail());

        return new RefreshResponse(newAccessToken);
    }
}
