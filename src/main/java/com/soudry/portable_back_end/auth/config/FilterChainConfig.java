package com.soudry.portable_back_end.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// import com.soudry.portable_back_end.jwt.JwtFacade;

import com.soudry.portable_back_end.jwt.services.JwtServices;

// @Configuration
// public class FilterChainConfig {

//     private final JwtServices jwtServices;

//     public FilterChainConfig(JwtServices jwtServices) {
//         this.jwtServices = jwtServices;
//     }
//     @Bean
//     @Order(1)
//     SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/public/**")
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(session ->
//                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/public/**").permitAll()
//                 .anyRequest().authenticated()
//             );
//         return http.build();
//     }

//     @Bean
//     @Order(2)
//     SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {
//         http
//             .securityMatcher("/private/**")
//             .csrf(csrf -> csrf.disable())
//             .sessionManagement(session ->
//                 session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/admin/**").authenticated().
//                 .requestMatchers("/private/**").authenticated()
//                 .anyRequest().authenticated()
//             )
//             .oauth2ResourceServer(oauth2 -> oauth2.jwt(
//                 jwt -> jwt.jwtAuthenticationConverter(jwtServices.getJwtAuthenticationConverter())
//             )
//         );
//         return http.build();
//     }
//      @Bean
//     PasswordEncoder passwordEncoder() {
//         return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//     }

//     @Bean
//     AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//         return configuration.getAuthenticationManager();
//     }
// }

@Configuration
public class FilterChainConfig {

    private final JwtServices jwtServices;

    public FilterChainConfig(JwtServices jwtServices) {
        this.jwtServices = jwtServices;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/private/**").authenticated()
                .anyRequest().authenticated()
            )

            .oauth2ResourceServer(oauth2 -> oauth2.jwt(
                jwt -> jwt.jwtAuthenticationConverter(jwtServices.getJwtAuthenticationConverter())
            ));

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}