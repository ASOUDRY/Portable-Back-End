package com.soudry.portable_back_end.auth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

@Service
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    public AuthServices(AuthenticationManager authenticationManager) {
       this.authenticationManager = authenticationManager;

    }

    public Authentication getAuthentication(String username, String password) {
         Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password)
        );
    return authentication;
    }
}