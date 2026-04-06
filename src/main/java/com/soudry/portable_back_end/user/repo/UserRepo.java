package com.soudry.portable_back_end.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, String> {
    Optional<Users> findByName(String name);
    boolean existsByName(String name);
    Optional<Users> findById(String id);
    // boolean deleteById(String  id);
    boolean existsById(String id);
}
