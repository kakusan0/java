package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String LOGIN_PAGE = "/login";
    private static final String LIST_PAGE = "/list";
    private static final String ROOT_PATH = "/";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ROOT_PATH, LOGIN_PAGE).permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form // フォームログインの設定を追加
                        .loginPage(LOGIN_PAGE) // ログインページのパスを指定
                        .defaultSuccessUrl("/list", true) // ログイン成功時のリダイレクト先を"/hello"に設定
                )
                .rememberMe(rememberMe -> rememberMe
                        .key("mySecretKey")
                        .tokenValiditySeconds(1209600)
                        .userDetailsService(userDetailsService)
                )
                .oauth2Login(oauth -> oauth.defaultSuccessUrl("/list", true)) // OAuth2ログイン成功時のリダイレクト先も"/hello"に変更
                .logout(logout -> logout
                        .logoutSuccessUrl(ROOT_PATH)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
//        UserDetails user = User.builder()
//                .username("user")
//                .password(passwordEncoder.encode("password"))
//                .roles("USER")
//                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(
//                user,
                admin
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}