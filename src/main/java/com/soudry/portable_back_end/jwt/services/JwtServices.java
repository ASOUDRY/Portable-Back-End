package com.soudry.portable_back_end.jwt.services;

import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;

@Service
public class JwtServices {

    private final JwtEncoder jwtEncoder;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public JwtServices(JwtEncoder jwtEncoder,
                       JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtEncoder = jwtEncoder;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    public JwtAuthenticationConverter getJwtAuthenticationConverter() {
        return jwtAuthenticationConverter;
    }

    public String generateJwt(Authentication authentication) {
        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow()
                .replace("ROLE_", ""); // prevent double prefix
        return generateJwt(authentication.getName(), role);
    }

    public String generateJwt(String username, String role) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .claim("scope", List.of("ROLE_" + role))
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims))
                .getTokenValue();
    }
}