package com.soudry.portable_back_end.user.admin;
import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.user.AccountRole;
import com.soudry.portable_back_end.user.repo.UserRepo;
import java.util.List;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.Optional;

@Service
public class AdminService {
    private final UserRepo userRepo;
    public AdminService(UserRepo userRepo) {
        this.userRepo = userRepo;
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
    
}
