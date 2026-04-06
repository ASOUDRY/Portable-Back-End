package com.soudry.portable_back_end.user.admin;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.auth.tokens.RefreshTokenRepo;
import com.soudry.portable_back_end.user.AccountRole;
import com.soudry.portable_back_end.user.repo.UserRepo;
import java.util.List;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.Optional;
import jakarta.transaction.Transactional;

@Service
public class AdminService {
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final PasswordEncoder passwordEncoder;
    public AdminService(UserRepo userRepo, RefreshTokenRepo refreshTokenRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.passwordEncoder = passwordEncoder;
    }
    public List<UserResponse> getAllUsers() {
        return userRepo.findAll()
                .stream().map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name()
                ))
                .toList();
    }
     public Optional<UserResponse> promoteToAdmin(String id) {
        return userRepo.findById(id).map(user -> {
            user.setRole(AccountRole.ADMIN);
            Users saved = userRepo.save(user);
            return new UserResponse(
                    saved.getId(),
                    saved.getName(),
                    saved.getEmail(),
                    saved.getRole().name()
            );
        });
    }
    public Optional<Users> updateAnyUser(UpdateAnyUser request) {
        return userRepo.findById(request.id()).map(existingUser -> {
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
    public boolean deleteAnyUser(String id) {
        if (!userRepo.existsById(id)) {
            return false;
        }
        var user = userRepo.findById(id).get();
        refreshTokenRepo.deleteByUser(user);
        userRepo.deleteById(id);
        return true;
    }    
}