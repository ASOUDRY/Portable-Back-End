package com.soudry.portable_back_end.jwt;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;
import com.soudry.portable_back_end.jwt.services.JwtServices;
@Component
public class JwtFacade {

    private final JwtServices jwtServices;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public JwtFacade(JwtServices jwtServices, JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtServices = jwtServices;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    public String retreiveJwt(Authentication authentication) {
        return jwtServices.generateJwt(authentication);
    }

    public JwtAuthenticationConverter getJwtAuthenticationConverter() {
        return jwtAuthenticationConverter;
    }
}