package com.soudry.portable_back_end.auth.services;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import jakarta.annotation.Nullable;

public class UserDetailsWithId implements UserDetails {
    private final String id;
    private final String username;
    private final String password;
    private final List<? extends GrantedAuthority> authorities;
    public UserDetailsWithId(String id, String username, String password,
        List<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getId() {
        return id;
    }
    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public @Nullable String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
}