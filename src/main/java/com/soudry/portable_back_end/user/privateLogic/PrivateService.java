package com.soudry.portable_back_end.user.privateLogic;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.auth.tokens.RefreshTokenRepo;
import com.soudry.portable_back_end.user.controllerDto.UpdateSelf;
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
    public Optional<Users> updateUser(UpdateSelf request, String id) {
        return userRepo.findById(id).map(existingUser -> {
            if (request.username() != null && !request.username().isBlank()) {
                existingUser.setName(request.username());
            }
            if (request.email() != null && !request.email().isBlank()) {
                existingUser.setEmail(request.email());
            }
            if (request.newPassword() != null && !request.newPassword().isBlank()) {
                String encodedPassword = passwordEncoder.encode(request.newPassword());
                existingUser.setPassword(encodedPassword);
            }
            return userRepo.save(existingUser);
        });
    }
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