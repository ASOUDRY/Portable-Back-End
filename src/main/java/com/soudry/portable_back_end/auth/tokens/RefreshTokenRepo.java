package com.soudry.portable_back_end.auth.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.soudry.portable_back_end.user.repo.Users;

import java.util.Optional;
@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, String> {
        Optional<RefreshToken> findByToken(String token);
        void deleteByUser(Users user);    
} 