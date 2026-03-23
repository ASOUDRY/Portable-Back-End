package com.soudry.portable_back_end.user.loginLogic;

import org.springframework.stereotype.Service;
import com.soudry.portable_back_end.auth.AuthFacade;

@Service
public class LoginService {

    private final AuthFacade authFacade;

    public LoginService(AuthFacade authFacade) {
        this.authFacade = authFacade;
        // this.authenticationManager = authenticationManager;
    }

    public String login(LoginDto dto) {
        String jwt = authFacade.authenticate(dto.username(), dto.password());
        return jwt;
    }   
}