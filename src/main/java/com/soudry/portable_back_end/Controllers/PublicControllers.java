package com.soudry.portable_back_end.Controllers;
import java.time.Instant;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.soudry.portable_back_end.dto.loginDto;
import com.soudry.portable_back_end.dto.registerDto;
import com.soudry.portable_back_end.entities.Users;
import com.soudry.portable_back_end.repo.UserRepo;

@RestController
@RequestMapping("/public")
public class PublicControllers {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;

    public PublicControllers(
        UserRepo userRepo, 
        PasswordEncoder passwordEncoder,
        AuthenticationManager authenticationManager,
        JwtEncoder jwtEncoder
    ) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }

    @GetMapping("/free")
    public ResponseEntity<String> free() {
        return ResponseEntity.status(HttpStatus.OK).body("Can you see this");
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody registerDto dto) {
        if (userRepo.existsByName(dto.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        String username = dto.username();
        String password = passwordEncoder.encode(dto.password());
        Users user = new Users(username, password, "User", dto.email());

        userRepo.save(user);
        String response = String.format("Thank you for registering with %s", username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

     @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody loginDto dto) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(dto.username(), dto.password())
        );

        String token = generateJwt(authentication);
        return ResponseEntity.ok(token);
    }

    private String generateJwt(Authentication authentication) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
            .subject(authentication.getName())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(3600))
            .claim("scope", "ROLE_USER")
            .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}