package com.soudry.portable_back_end.auth.tokens;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.soudry.portable_back_end.user.repo.Users;

import java.util.Optional;
@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken, String> {
        Optional<RefreshToken> findByToken(String token);
        @Modifying
        @Query("DELETE FROM RefreshToken r WHERE r.user = :user")
        void deleteByUser(@Param("user") Users user);
} 