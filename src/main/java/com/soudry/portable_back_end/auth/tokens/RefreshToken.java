package com.soudry.portable_back_end.auth.tokens;

import java.time.Instant;
import com.soudry.portable_back_end.user.repo.Users;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;

@Entity
public class RefreshToken {
    @Id
    private String id;

    private String token;

    @ManyToOne
    private Users user;

    private Instant expiresAt;

    private boolean revoked;

    public RefreshToken() {};

    public RefreshToken(String id, String token, Instant expiresAt, boolean revoked) {
        this.id = id;
        this.token = token;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public boolean getRevoked() {
        return revoked;
    }

    public String getToken() {
        return token;
    }
}
