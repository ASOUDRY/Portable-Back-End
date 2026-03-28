package com.soudry.portable_back_end.jwt.services;

import java.time.Instant;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
// import org.springframework.security.core.Authentication;

@Service
public class JwtServices {

    private JwtEncoder jwtEncoder;
    public JwtServices(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    //  public String generateJwt(Authentication authentication) {
    //     Instant now = Instant.now();

    //     JwtClaimsSet claims = JwtClaimsSet.builder()
    //         .subject(authentication.getName())
    //         .issuedAt(now)
    //         .expiresAt(now.plusSeconds(3600))
    //         .claim("scope", "ROLE_USER")
    //         .build();

    //     JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

    //     return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    // }

    public String generateJwt(String userId, String role) {
    Instant now = Instant.now();

    JwtClaimsSet claims = JwtClaimsSet.builder()
        .subject(userId)
        .issuedAt(now)
        .expiresAt(now.plusSeconds(3600))
        .claim("scope", role)
        .build();

    JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

    return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
}
}