package com.dark869.auth_api.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.dark869.auth_api.dto.ValidateEmailResponse;
import com.dark869.auth_api.model.EmailVerificationToken;
import com.dark869.auth_api.model.User;
import com.dark869.auth_api.repository.EmailVerificationTokenRepository;
import com.dark869.auth_api.repository.UserRepository;
import com.dark869.auth_api.utils.INotification;

@Service
public class EmailService implements INotification {
    private final MailSender mailSender;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final UserRepository userRepository;
    private final String serverUrl;
    private final String senderEmail;

    public EmailService(MailSender mailSender,
            EmailVerificationTokenRepository emailVerificationTokenRepository,
            UserRepository userRepository,
            @Value("${server.url}") String serverUrl,
            @Value("${app.mail.from:no-reply@localhost}") String senderEmail) {
        this.mailSender = mailSender;
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
        this.userRepository = userRepository;
        this.serverUrl = serverUrl;
        this.senderEmail = senderEmail;
    }

    @Async
    public void sendNotification(String to, String subject, UUID userId) {
        SimpleMailMessage message = new SimpleMailMessage();
        UUID token = UUID.randomUUID();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token.toString())
                .user(user)
                .used(false)
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        message.setFrom(senderEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText("Please verify your email to complete registration. \nYour verification code is: "
                + token.toString()
                + "\nThis code will expire in 15 minutes. \n Send your verification code to "
                + serverUrl
                + "/api/auth/verify-email endpoint to verify your email.");
        try {
            mailSender.send(message);
        } catch (MailException e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    // public ValidateEmailResponse validateEmail(UUID token) {
    // EmailVerificationToken verificationToken =
    // emailVerificationTokenRepository.findByToken(token.toString())
    // .orElseThrow(() -> new IllegalArgumentException("Invalid verification
    // token"));

    // }
}
