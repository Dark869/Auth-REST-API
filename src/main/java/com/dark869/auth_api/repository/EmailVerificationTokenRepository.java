package com.dark869.auth_api.repository;

import com.dark869.auth_api.model.EmailVerificationToken;
import com.dark869.auth_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM EmailVerificationToken evt WHERE evt.user = :user")
    void deleteAllByUser(User user);
}
