package com.soudry.portable_back_end.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.soudry.portable_back_end.user.registerLogic.RegisterService;
import com.soudry.portable_back_end.auth.services.AuthServices;
import com.soudry.portable_back_end.auth.tokens.AccessRefreshTokens;
import com.soudry.portable_back_end.user.loginLogic.LoginDto;
import com.soudry.portable_back_end.user.loginLogic.authorizedDto;
import com.soudry.portable_back_end.user.registerLogic.RegisterDto;
// import org.springframework.web.bind.annotation.DeleteMapping;
import com.soudry.portable_back_end.user.controllerDto.*;
import java.util.Optional;

@RestController
@RequestMapping("/public")
public class UserController {

    private final RegisterService registerService;
    private final AuthServices authServices;

    public UserController(RegisterService registerService, AuthServices authServices) {
        this.registerService = registerService;
        this.authServices = authServices;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDto dto) {
        if (registerService.accountExists(dto.username())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("Account already exists"));
        }
        String username = registerService.register(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Thank you for registering with us " + username));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        Optional<authorizedDto> result = authServices.loginAndAuthenticate(dto);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid credentials"));
        }
        return ResponseEntity.ok(result.get());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {
        Optional<AccessRefreshTokens> tokens = authServices.refreshAccessToken(request.refreshToken());
        if (tokens.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Invalid or expired refresh token"));
        }
        return ResponseEntity.ok(tokens.get());
    }

  
}
