package com.soudry.portable_back_end.repo;

import com.soudry.portable_back_end.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByName(String name);
    boolean existsByName(String name);
}
