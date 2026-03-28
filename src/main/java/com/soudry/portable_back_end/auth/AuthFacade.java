package com.soudry.portable_back_end.auth;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.soudry.portable_back_end.auth.services.AuthServices;
import com.soudry.portable_back_end.auth.services.RefreshTokenService;
import com.soudry.portable_back_end.auth.services.TokenService;
import com.soudry.portable_back_end.auth.tokens.AccessRefreshTokens;
import com.soudry.portable_back_end.user.loginLogic.RefreshDto;

@Component
public class AuthFacade {

    private final AuthServices authServices;
    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;
    // private final 
    

    public AuthFacade(AuthServices authServices, TokenService tokenService, RefreshTokenService refreshTokenService) {
        this.authServices = authServices;
        this.tokenService = tokenService;
        this.refreshTokenService = refreshTokenService;
    }

    public AccessRefreshTokens authenticate(String username, String password) {
        Authentication a = getAuthentication(username, password);
        String accessToken = tokenService.generateAccessToken(a);
        String idAndRefreshToken = tokenService.generateIdAndRefreshToken(username);

        return new AccessRefreshTokens(accessToken, idAndRefreshToken);
    }

    public Authentication getAuthentication(String username, String password) {
        Authentication aS = authServices.getAuthentication(username, password);
        return aS;
    }

    public String refreshAccessToken(RefreshDto dto) {
        if (refreshTokenService.validate(dto.refreshToken())) {
              String accessToken = tokenService.refreshAccessToken(dto);
              return accessToken;
        }
        return null;
    }
}