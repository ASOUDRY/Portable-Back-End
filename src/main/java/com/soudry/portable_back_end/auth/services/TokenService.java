package com.soudry.portable_back_end.auth.services;
import org.springframework.stereotype.Service;

import com.soudry.portable_back_end.jwt.JwtFacade;
import com.soudry.portable_back_end.user.UserFacade;
import com.soudry.portable_back_end.user.loginLogic.RefreshDto;

import org.springframework.security.core.Authentication;

@Service
public class TokenService {

      private final JwtFacade jwtFacade;
      private final RefreshTokenService refreshTokenService;
      private final UserFacade userFacade;

      public TokenService(JwtFacade jwtFacade, RefreshTokenService refreshTokenService, UserFacade userFacade) {
        this.jwtFacade = jwtFacade;
        this.refreshTokenService = refreshTokenService;
        this.userFacade = userFacade;
      }

    public String generateAccessToken(Authentication auth) {
        return jwtFacade.retreiveJwt(auth);
    }

    public String refreshAccessToken(RefreshDto dto) {
        var user = userFacade.retreiveUserById(dto.id());
        return jwtFacade.refreshJwt(user);
    }

    public String generateIdAndRefreshToken(String username) {
        var u = userFacade.retreiveUser(username);
        String idAndRefreshToken = refreshTokenService.createIdandRefreshToken(u);
        return idAndRefreshToken;
    }
}