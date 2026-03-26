package com.soudry.portable_back_end.user.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;

@RestController
@RequestMapping("/private")
public class PrivateControllers {

    private final UserRepo userRepo;

    public PrivateControllers(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // UPDATE user
    @PutMapping("/users/{name}")
    public ResponseEntity<Users> updateUser(@PathVariable String name, @RequestBody Users updatedUser) {

        Optional<Users> existingUser = userRepo.findByName(name);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Users user = existingUser.get();

        // update fields (adjust based on your entity)
        user.setName(updatedUser.getName());
        user.setPassword(updatedUser.getPassword());

        Users savedUser = userRepo.save(user);

        return ResponseEntity.ok(savedUser);
    }

    // DELETE user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {

        if (!userRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepo.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}