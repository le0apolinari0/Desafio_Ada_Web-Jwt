package br.com.modulo.web2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    public boolean verifyEmail(String token) {
        log.info("Verifying email with token: {}", token);
        if (token == null || token.trim().isEmpty()) {
            log.warn("Empty or null token provided");
            return false;
        }

        boolean isValid = token.startsWith("valid_") && token.length() > 10;

        if (isValid) {
            log.info("Email verification successful for token: {}", token);
        } else {
            log.warn("Email verification failed for token: {}", token);
        }

        return isValid;
    }

    public String generateVerificationToken(Long userId, String email) {
        String token = "valid_" + System.currentTimeMillis() + "_" + userId;
        log.info("Generated verification token for user {}: {}", userId, token);
        return token;
    }
}








