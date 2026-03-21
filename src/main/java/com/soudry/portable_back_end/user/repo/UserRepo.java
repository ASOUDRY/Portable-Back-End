package com.soudry.portable_back_end.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soudry.portable_back_end.user.entities.Users;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);
    boolean existsByName(String name);
}
