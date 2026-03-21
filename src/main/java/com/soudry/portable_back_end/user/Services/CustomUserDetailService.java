package com.soudry.portable_back_end.user.Services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

import com.soudry.portable_back_end.user.entities.Users;
import com.soudry.portable_back_end.user.repo.UserRepo;

@Service
public class CustomUserDetailService implements UserDetailsService {
   private final UserRepo userRepo;

    public CustomUserDetailService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepo.findByName(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.builder()
            .username(user.getName())
            .password(user.getPassword())
            .roles(user.getRole())
            .build();
    }
}