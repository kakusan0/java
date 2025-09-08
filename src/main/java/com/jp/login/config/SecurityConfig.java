package com.jp.login.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.webauthn.authentication.PublicKeyCredentialRequestOptionsRepository;
import org.springframework.security.web.webauthn.registration.HttpSessionPublicKeyCredentialCreationOptionsRepository;
import org.springframework.security.web.webauthn.registration.PublicKeyCredentialCreationOptionsRepository;

@Configuration
@Profile("!dev")
class NoSecurityConfig {

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                                .csrf(csrf -> csrf.disable())
                                .formLogin(form -> form.disable())
                                .httpBasic(basic -> basic.disable())
                                .build();
        }
}

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity()
@Profile("dev")
public class SecurityConfig {

        public static final String LOGIN_PROCESSING_URL = "/login";
        @Value("/register")
        private static final String REGISTER_PAGE = "/register";

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                FormLoginMethodCheckFilter methodCheckFilter = new FormLoginMethodCheckFilter();

                return http
                                .addFilterBefore(methodCheckFilter, UsernamePasswordAuthenticationFilter.class)
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**", "/userName", "/userNameCheck","/webauthn/**","/webauthn/authentication/options","/webauthn/authentication/verify"
                                                )
                                                .permitAll()
                                                .requestMatchers(LOGIN_PROCESSING_URL).hasAuthority("ROLE_UserCheckOK")
                                                .requestMatchers(REGISTER_PAGE).hasAuthority("ROLE_ADMIN")
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
                    .webAuthn(webAuthn -> webAuthn
                        .rpName("Local RP")
                        .rpId("localhost")
                        .allowedOrigins("http://localhost:8080/userName")
                        // 登録用は CreationOptionsRepository（registration）
//                        .creationOptionsRepository(creationOptionsRepository())
//                            .requestOptionsRepository
//                                (requestOptionsRepository())
                    )


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

    @Bean
    PublicKeyCredentialCreationOptionsRepository creationOptionsRepository() {
        // 登録用は標準のセッション実装を使用
        return new HttpSessionPublicKeyCredentialCreationOptionsRepository();
    }

    @Bean
    PublicKeyCredentialRequestOptionsRepository requestOptionsRepository() {
        // 認証用は既存のカスタム実装を使用
        return new CustomPublicKeyCredentialRequestOptionsRepository();
    }


}
