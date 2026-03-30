package com.soudry.portable_back_end.auth.services;

import java.util.Optional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.auth.tokens.AccessRefreshTokens;
import com.soudry.portable_back_end.auth.tokens.RefreshToken;
import com.soudry.portable_back_end.auth.tokens.RefreshTokenRepo;
import com.soudry.portable_back_end.jwt.services.JwtServices;
import com.soudry.portable_back_end.user.loginLogic.LoginDto;
import com.soudry.portable_back_end.user.loginLogic.authorizedDto;
import com.soudry.portable_back_end.user.repo.UserRepo;
import com.soudry.portable_back_end.user.repo.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@Service
public class AuthServices {

    private final RefreshTokenService refreshTokenService;
    private final UserRepo userRepo;
    private final RefreshTokenRepo refreshTokenRepo;
    private final JwtServices jwtServices;
    private final AuthenticationManager authenticationManager;

    public AuthServices(AuthenticationManager authenticationManager,
                        RefreshTokenService refreshTokenService,
                        UserRepo userRepo,
                        RefreshTokenRepo refreshTokenRepo,
                        JwtServices jwtServices) {

        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.userRepo = userRepo;
        this.refreshTokenRepo = refreshTokenRepo;
        this.jwtServices = jwtServices;
    }

    // ---------------- REFRESH ----------------

    public Optional<AccessRefreshTokens> refreshAccessToken(String refreshToken) {
        Optional<String> tokenIdOpt = refreshTokenService.validate(refreshToken);
        if (tokenIdOpt.isEmpty()) return Optional.empty();
        Optional<RefreshToken> tokenOpt = refreshTokenRepo.findById(tokenIdOpt.get());
        if (tokenOpt.isEmpty()) return Optional.empty();
        RefreshToken token = tokenOpt.get();
        Users user = token.getUser();
        String rotatedToken = rotateToken(token.getId(), user);
        String accessToken = jwtServices.generateJwt(user.getName(), user.getRole().name());
        return Optional.of(new AccessRefreshTokens(accessToken, rotatedToken));
    }

    // ---------------- ROTATE ----------------

    public String rotateToken(String tokenId, Users user) {
        refreshTokenService.deleteToken(tokenId);
        return refreshTokenService.generateRefreshToken(user);
    }

    // ---------------- LOGIN ----------------

    public Optional<authorizedDto> loginAndAuthenticate(LoginDto dto) {
        Optional<AccessRefreshTokens> tokens =
                authenticate(dto.username(), dto.password());
        if (tokens.isEmpty()) return Optional.empty();
        Optional<Users> userOpt = userRepo.findByName(dto.username());
        if (userOpt.isEmpty()) return Optional.empty(); // safety
        Users user = userOpt.get();
        AccessRefreshTokens art = tokens.get();
        return Optional.of(
                new authorizedDto(
                        user.getId(),
                        art.accessToken(),
                        art.refreshToken()
                )
        );
    }

    // ---------------- AUTH ----------------

    public Optional<AccessRefreshTokens> authenticate(String username, String password) {
        try {
            Authentication auth = getAuthentication(username, password);
            String accessToken = jwtServices.generateJwt(auth);
            String refreshToken = refreshToken(username);
            return Optional.of(new AccessRefreshTokens(accessToken, refreshToken));
        } catch (AuthenticationException ex) {
            return Optional.empty();
        }
    }

    public Authentication getAuthentication(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    // ---------------- REFRESH TOKEN CREATE ----------------

    public String refreshToken(String username) {
        return userRepo.findByName(username)
                .map(refreshTokenService::generateRefreshToken)
                .orElseThrow(); // safe assumption after auth
    }

    // ---------------- LOGOUT ----------------

    public boolean logOut(String refreshToken) {
        Optional<String> tokenIdOpt = refreshTokenService.validate(refreshToken);
        if (tokenIdOpt.isEmpty()) return false;
        refreshTokenService.deleteToken(tokenIdOpt.get());
        return true;
    }
}