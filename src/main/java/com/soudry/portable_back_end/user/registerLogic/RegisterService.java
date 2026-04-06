package com.soudry.portable_back_end.user.registerLogic;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;
import com.soudry.portable_back_end.user.AccountRole;

@Service
public class RegisterService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public RegisterService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String register(RegisterDto dto) {

        if (userRepo.existsByName(dto.username())) {
            throw new IllegalStateException("User already exists");
        }

        if (dto.username() == null || dto.username().isBlank()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (dto.password() == null || dto.password().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }

        String encodedPassword = passwordEncoder.encode(dto.password());

        Users user = new Users(
                dto.username(),
                encodedPassword,
                AccountRole.USER,
                dto.email()
        );

        userRepo.save(user);

        return user.getName();
    }

    public boolean accountExists(String username) {
        return userRepo.existsByName(username);
    }
}