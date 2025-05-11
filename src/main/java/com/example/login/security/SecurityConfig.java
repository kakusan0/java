package com.example.login.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfig {

    public static final String LOGIN_PROCESSING_URL = "/login";
    private static final String PUBLIC_PAGE = "/public";
    private static final String REGISTER_PAGE = "/register";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                        .requestMatchers(LOGIN_PROCESSING_URL).permitAll()
                        .requestMatchers(REGISTER_PAGE).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PROCESSING_URL)
                        .successHandler(customAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .sessionManagement(session -> session
                        .invalidSessionUrl(LOGIN_PROCESSING_URL)
                        .sessionFixation()
                        .migrateSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl(LOGIN_PROCESSING_URL))
                .logout(logout -> logout
                        .logoutSuccessUrl(LOGIN_PROCESSING_URL).invalidateHttpSession(true)
                        .deleteCookies("SESSION")
                )
//                .csrf(csrf -> csrf.disable())
                .build();
    }

    @Bean
    public org.springframework.security.web.authentication.AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            if (isAdmin) {
                response.sendRedirect(REGISTER_PAGE);
            } else {
                response.sendRedirect(PUBLIC_PAGE);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder
    passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}