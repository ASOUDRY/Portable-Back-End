package com.soudry.portable_back_end.auth.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;
import java.util.List;

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
           return new UserDetailsWithId(
        user.getId(),
        user.getName(),
        user.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
    );
    }
}