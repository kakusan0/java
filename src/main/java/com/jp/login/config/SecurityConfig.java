package com.jp.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.jp.login.constants.ApplicationConstants;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfig {
        private static final String REGISTER_PAGE = "/register";

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**",
                                                                "/webauthn/**",
                                                                ApplicationConstants.USERNAME_CHECK,
                                                                ApplicationConstants.USER_CHECK,
                                                                ApplicationConstants.LOGIN)
                                                .permitAll()
                                                .requestMatchers(REGISTER_PAGE).hasAuthority("ROLE_ADMIN")
                                                .anyRequest().authenticated())
                                .exceptionHandling(e -> e.accessDeniedPage(
                                                ApplicationConstants.USERNAME_CHECK))
                                .sessionManagement(session -> session
                                                .invalidSessionUrl(
                                                                ApplicationConstants.USERNAME_CHECK)
                                                .sessionFixation()
                                                .migrateSession()
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(true)
                                                .expiredUrl(ApplicationConstants.USERNAME_CHECK))
                                .formLogin(form -> form
                                                .loginPage(ApplicationConstants.LOGIN)
                                                .defaultSuccessUrl(
                                                                ApplicationConstants.MAIN, false))
                                .webAuthn(webAuthn -> webAuthn
                                                .rpName("Local RP")
                                                .rpId("localhost")
                                                .allowedOrigins("http://localhost:8080/userName"))
                                .logout(logout -> logout
                                                .logoutSuccessUrl(
                                                                ApplicationConstants.USERNAME_CHECK)
                                                .invalidateHttpSession(true)
                                                .deleteCookies("SESSION"))
                                .build();
        }

        @Bean
        BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
