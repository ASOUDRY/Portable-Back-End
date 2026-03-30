package com.soudry.portable_back_end.auth.tokens;

import java.time.Instant;
import com.soudry.portable_back_end.user.repo.Users;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Entity;

@Entity
public class RefreshToken {
    @Id
    private String id;
    private String token;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;
    private Instant expiresAt;
    public RefreshToken() {};

    public RefreshToken(String id, String token, Instant expiresAt, Users users) {
        this.id = id;
        this.token = token;
        this.expiresAt = expiresAt;
        this.user = users;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Users getUser() {
        return user;
    }
}
