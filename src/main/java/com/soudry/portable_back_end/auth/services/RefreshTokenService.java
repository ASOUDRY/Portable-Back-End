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

    public String createIdandRefreshToken(Users user) {
        String id = UUID.randomUUID().toString();           // selector
        String rawToken = UUID.randomUUID().toString();     // secret
        String hashedToken = passwordEncoder.encode(rawToken);
        Instant expiresAt = Instant.now().plus(7, ChronoUnit.DAYS);
        RefreshToken rToken = new RefreshToken(id, hashedToken, expiresAt, false);
        refreshTokenRepo.save(rToken);
        return id + "." + rawToken;
    }

    public boolean validate(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) return false;
        String id = parts[0];
        String rawToken = parts[1];
        Optional<RefreshToken> optional = refreshTokenRepo.findById(id);
        if (optional.isEmpty()) return false;
        RefreshToken rf = optional.get();
        if (rf.getExpiresAt().isBefore(Instant.now())) return false;
        if (rf.getRevoked()) return false;
        return passwordEncoder.matches(rawToken, rf.getToken());
}

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
    }
}
