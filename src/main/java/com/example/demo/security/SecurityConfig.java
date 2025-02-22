package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String LOGIN_PAGE = "/login";
    private static final String HOME_PAGE = "/home";
    private static final String ROOT_PATH = "/";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // 認可設定
        configureAuthorization(httpSecurity);

        // OAuth2ログイン設定
        configureOAuth2Login(httpSecurity);

        // ログアウト設定
        configureLogout(httpSecurity);

        return httpSecurity.build();
    }

    private void configureAuthorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers(ROOT_PATH, LOGIN_PAGE).permitAll()
                .anyRequest().authenticated()
        );
    }

    private void configureOAuth2Login(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.oauth2Login(oauth -> oauth.defaultSuccessUrl(HOME_PAGE, true)
        );
    }

    private void configureLogout(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.logout(logout -> logout
                .logoutSuccessUrl(ROOT_PATH)
                .deleteCookies("JSESSIONID")
        );
    }
}