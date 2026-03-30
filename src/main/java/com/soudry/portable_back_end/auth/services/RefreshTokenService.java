package com.soudry.portable_back_end.auth.services;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.auth.tokens.RefreshToken;
import com.soudry.portable_back_end.auth.tokens.RefreshTokenRepo;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.Optional;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;

    public RefreshTokenService(RefreshTokenRepo refreshTokenRepo, PasswordEncoder passwordEncoder) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------- GENERATE ----------------

    public String generateRefreshToken(Users user) {
        String id = UUID.randomUUID().toString();          // selector
        String rawToken = UUID.randomUUID().toString();    // secret
        String hashedToken = passwordEncoder.encode(rawToken);
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshToken token = new RefreshToken(
                id,
                hashedToken,
                expiresAt,
                user
        );
        refreshTokenRepo.save(token);
        return id + "." + rawToken;
    }

    // ---------------- VALIDATE ----------------

    public Optional<String> validate(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        String[] parts = token.split("\\.");
        if (parts.length != 2) return Optional.empty();
        String id = parts[0];
        String rawToken = parts[1];
        return refreshTokenRepo.findById(id)
                .filter(rf -> !rf.getExpiresAt().isBefore(Instant.now()))
                .filter(rf -> passwordEncoder.matches(rawToken, rf.getToken()))
                .map(RefreshToken::getId);
    }

    // ---------------- DELETE ----------------

    public void deleteToken(String tokenId) {
        refreshTokenRepo.deleteById(tokenId);
    }
}
