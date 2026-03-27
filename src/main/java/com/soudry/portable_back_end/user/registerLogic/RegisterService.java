package com.soudry.portable_back_end.user.registerLogic;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;

@Service
public class RegisterService {
        private final UserRepo userRepo;
        private final PasswordEncoder passwordEncoder;

        public RegisterService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
            this.userRepo = userRepo;
            this.passwordEncoder = passwordEncoder;
        }

        public String register(RegisterDto dto) {
        String username = dto.username();
        String password = passwordEncoder.encode(dto.password());
        Users user = new Users(username, password, "User", dto.email());
        userRepo.save(user);
        return username;
        }

        public boolean accountExists(String username) {
            return userRepo.existsByName(username);
        }
}
