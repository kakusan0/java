package com.jp.login.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
public class SecurityConfig {

    public static final String LOGIN_PROCESSING_URL = "/login";
    private static final String REGISTER_PAGE = "/register";

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        FormLoginMethodCheckFilter methodCheckFilter = new FormLoginMethodCheckFilter();

    	return http
                .addFilterBefore(methodCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**","/userName","/userNameCheck").permitAll()
                        .requestMatchers(LOGIN_PROCESSING_URL).hasAuthority("ROLE_UserCheckOK")
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
                        .defaultSuccessUrl(REGISTER_PAGE, false)
                        .failureHandler(new SimpleUrlAuthenticationFailureHandler()))
                .logout(logout -> logout
                        .logoutSuccessUrl("/userName")
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION"))
                .build();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}