package com.jp.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jp.login.dto.PublicKeyCredentialRequestOptions;
import com.jp.login.service.WebAuthnService;
import com.jp.login.service.SimpleWebAuthnSessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Slf4j
public class WebAuthnLoginController {

    private final WebAuthnService webAuthnService;
    private final SimpleWebAuthnSessionRepository sessionRepository;

    /**
     * パスキー認証を完了する
     */
    @PostMapping("/webauthn")
    public ResponseEntity<?> completeAuthentication(
            @RequestBody Map<String, Object> credential,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            log.info("WebAuthn authentication completion requested");
            
            // セッションからオプションを取得
            PublicKeyCredentialRequestOptions options = sessionRepository.loadRequestOptions(request);
            if (options == null) {
                return ResponseEntity.badRequest().body(Map.of("authenticated", false, "message", "No authentication options found in session"));
            }

            String authenticatedUsername = webAuthnService.processAuthentication(credential, options, request, response);
            
            if (authenticatedUsername != null) {
                log.info("User {} authenticated successfully with passkey", authenticatedUsername);
                return ResponseEntity.ok(Map.of(
                    "authenticated", true, 
                    "username", authenticatedUsername,
                    "redirectUrl", "/register" // デフォルトのリダイレクト先
                ));
            } else {
                return ResponseEntity.badRequest().body(Map.of("authenticated", false, "message", "Authentication failed"));
            }
            
        } catch (Exception e) {
            log.error("Error completing authentication", e);
            return ResponseEntity.badRequest().body(Map.of("authenticated", false, "message", "Authentication failed"));
        }
    }
}