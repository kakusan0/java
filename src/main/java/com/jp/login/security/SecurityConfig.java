package com.jp.login.security;

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
                        .requestMatchers("/userName").permitAll()
                        .requestMatchers("/userNameCheck").permitAll()
                        .requestMatchers("/login").permitAll()
//                        .requestMatchers(REGISTER_PAGE).hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .invalidSessionUrl("/userName")
                        .sessionFixation()
                        .migrateSession()
                        .maximumSessions(1)
                        .maxSessionsPreventsLogin(true)
                        .expiredUrl("/userName"))
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customAuthenticationSuccessHandler())
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/userName")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION"))
                .build();
    }

    @Bean
    public org.springframework.security.web.authentication.AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String username = authentication.getName();
            if ("admin".equals(username)) {
                // ユーザ名がadminの場合のみ管理ページへ
                response.sendRedirect(REGISTER_PAGE);
            } else {
                // それ以外のユーザ名の場合
                response.sendRedirect(PUBLIC_PAGE);
            }
        };
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}