package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public static final String LOGIN_PROCESSING_URL = "/login";
    private static final String LIST_PAGE = "/list";
    private static final String REGISTER_URL = "/register";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                        .requestMatchers(LOGIN_PROCESSING_URL, REGISTER_URL, REGISTER_URL + "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage(LOGIN_PROCESSING_URL)
                        .loginProcessingUrl(LOGIN_PROCESSING_URL)
                        .defaultSuccessUrl(LIST_PAGE, true)
                )
                .oauth2Login(oauth -> oauth
                        .loginPage(LOGIN_PROCESSING_URL)
                        .defaultSuccessUrl(LIST_PAGE, true))
                .logout(logout -> logout
                        .logoutSuccessUrl(LOGIN_PROCESSING_URL)
                        .deleteCookies("JSESSIONID")
                )
                .build();
    }

    @Bean
    public BCryptPasswordEncoder
    passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public SpringResourceTemplateResolver templateResolver() {
//        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setPrefix("classpath:/templates/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode(TemplateMode.HTML);
//        resolver.setCharacterEncoding("UTF-8");
//        return resolver;
//    }
}