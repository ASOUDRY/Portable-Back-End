package com.soudry.portable_back_end.user.privateLogic;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.auth.tokens.RefreshTokenRepo;
import com.soudry.portable_back_end.user.controllerDto.UpdateUserRequest;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class PrivateService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepo refreshTokenRepo;

    public PrivateService(UserRepo userRepo, PasswordEncoder passwordEncoder, RefreshTokenRepo refreshTokenRepo) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepo = refreshTokenRepo;
    }

    // ---------------- UPDATE USER ----------------

    public Optional<Users> updateUser(String id, UpdateUserRequest request) {

        return userRepo.findById(id).map(existingUser -> {

            if (request.username() != null && !request.username().isBlank()) {
                existingUser.setName(request.username());
            }

            if (request.email() != null && !request.email().isBlank()) {
                existingUser.setEmail(request.email());
            }

            if (request.password() != null && !request.password().isBlank()) {
                String encodedPassword = passwordEncoder.encode(request.password());
                existingUser.setPassword(encodedPassword);
            }

            return userRepo.save(existingUser);
        });
    }

    // ---------------- DELETE USER ----------------

    @Transactional
    public boolean deleteUser(String id) {
        if (!userRepo.existsById(id)) {
            return false;
        }
        var user = userRepo.findById(id).get();
        refreshTokenRepo.deleteByUser(user);
        userRepo.deleteById(id);
        return true;
    }
}