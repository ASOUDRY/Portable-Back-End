package com.soudry.portable_back_end.user.controllers;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.soudry.portable_back_end.user.privateLogic.PrivateService;
import com.soudry.portable_back_end.user.repo.Users;

import org.springframework.http.HttpStatus;

import com.soudry.portable_back_end.auth.services.AuthServices;
import com.soudry.portable_back_end.user.controllerDto.ErrorResponse;
import com.soudry.portable_back_end.user.controllerDto.RefreshRequest;
import  com.soudry.portable_back_end.user.controllerDto.UpdateUserRequest;

@RestController
@RequestMapping("/private")
public class PrivateControllers {

    private final PrivateService privateService;
    private final AuthServices authServices;
    public PrivateControllers(PrivateService privateService, AuthServices authServices) {
        this.privateService = privateService;
        this.authServices = authServices;
    }

    // ---------------- UPDATE USER ----------------

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UpdateUserRequest request) {
        Optional<Users> updatedUser = privateService.updateUser(id, request);
        if (updatedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.ok(updatedUser.get());
    }

    // ---------------- DELETE USER ----------------

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        boolean deleted = privateService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {
        boolean success = authServices.logOut(request.refreshToken());
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Token not found or already revoked"));}
        return ResponseEntity.noContent().build(); // 204
    }
}