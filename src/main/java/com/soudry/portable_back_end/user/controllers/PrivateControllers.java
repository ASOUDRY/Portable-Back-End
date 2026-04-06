package com.soudry.portable_back_end.user.controllers;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.soudry.portable_back_end.user.privateLogic.PrivateService;
import com.soudry.portable_back_end.user.repo.Users;
import org.springframework.http.HttpStatus;
import com.soudry.portable_back_end.auth.services.AuthServices;
import com.soudry.portable_back_end.user.controllerDto.ErrorResponse;
import com.soudry.portable_back_end.user.controllerDto.RefreshRequest;
import  com.soudry.portable_back_end.user.controllerDto.UpdateSelf;
import org.springframework.security.oauth2.jwt.Jwt;
@RestController
@RequestMapping("/private")
public class PrivateControllers {
    private final PrivateService privateService;
    private final AuthServices authServices;
    public PrivateControllers(PrivateService privateService, AuthServices authServices) {
        this.privateService = privateService;
        this.authServices = authServices;
    }
    @PatchMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestBody UpdateSelf request, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String id = jwt.getClaimAsString("userId");
        Optional<Users> updatedUser = privateService.updateUser(request, id);
        if (updatedUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.ok(updatedUser.get());
    }
    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String id = jwt.getClaimAsString("userId");
        boolean deleted = privateService.deleteUser(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found"));
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/logoutUser")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {
        boolean success = authServices.logOut(request.refreshToken());
        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Token not found."));}
        return ResponseEntity.noContent().build(); // 204
    }
}
