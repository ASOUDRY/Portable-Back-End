package com.soudry.portable_back_end.user.controllers;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.soudry.portable_back_end.user.privateLogic.PrivateService;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/private")
public class PrivateControllers {
    private final UserRepo userRepo;
    private final PrivateService privateService;
    public PrivateControllers(UserRepo userRepo, PrivateService privateService) {
        this.userRepo = userRepo; 
        this.privateService = privateService;
    }

    // UPDATE user
    @PatchMapping("/users/{id}")
    public ResponseEntity<Users> updateUser(@PathVariable String id, @RequestBody Users user) {
        Optional<Users> existingUser = userRepo.findById(id);
        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var updatedUser = privateService.updateUser(existingUser.get(), user);
        return ResponseEntity.ok(updatedUser);
    }

    // DELETE user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        if (!userRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}