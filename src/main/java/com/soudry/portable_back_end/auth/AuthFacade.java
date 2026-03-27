package com.soudry.portable_back_end.auth;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.soudry.portable_back_end.auth.services.AuthServices;
import com.soudry.portable_back_end.jwt.JwtFacade;

@Component
public class AuthFacade {

    private final AuthServices authServices;
    private final JwtFacade jwtFacade;

    public AuthFacade(AuthServices authServices, JwtFacade jwtFacade) {
        this.authServices = authServices;
        this.jwtFacade = jwtFacade;
    }

    public String authenticate(String username, String password) {
        Authentication a = getAuthentication(username, password);
        return fetchJwt(a);
    }

  
    public Authentication getAuthentication(String username, String password) {
        Authentication aS = authServices.getAuthentication(username, password);
        return aS;
    }

    private String fetchJwt(Authentication a) {
               return jwtFacade.retreiveJwt(a);
    }
}
