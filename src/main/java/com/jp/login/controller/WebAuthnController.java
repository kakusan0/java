package com.jp.login.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.jp.login.dto.PublicKeyCredentialCreationOptions;
import com.jp.login.dto.PublicKeyCredentialRequestOptions;
import com.jp.login.service.WebAuthnService;
import com.jp.login.service.SimpleWebAuthnSessionRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/webauthn")
@RequiredArgsConstructor
@Slf4j
public class WebAuthnController {

    private final WebAuthnService webAuthnService;
    private final SimpleWebAuthnSessionRepository sessionRepository;

    /**
     * パスキー登録用のオプションを生成する
     */
    @PostMapping("/register/options")
    public ResponseEntity<?> getRegistrationOptions(
            HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            log.info("WebAuthn registration options requested");
            
            // セッションからユーザー名を取得
            String username = webAuthnService.getCurrentUsername();
            if (username == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }

            PublicKeyCredentialCreationOptions options = webAuthnService.createRegistrationOptions(username);
            
            // セッションにオプションを保存
            sessionRepository.saveCreationOptions(request, response, options);
            
            return ResponseEntity.ok(options);
            
        } catch (Exception e) {
            log.error("Error creating registration options", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create registration options"));
        }
    }

    /**
     * パスキー登録を完了する
     */
    @PostMapping("/register")
    public ResponseEntity<?> completeRegistration(
            @RequestBody Map<String, Object> credential,
            HttpServletRequest request) {
        try {
            log.info("WebAuthn registration completion requested");
            
            // セッションからオプションを取得
            PublicKeyCredentialCreationOptions options = sessionRepository.loadCreationOptions(request);
            if (options == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "No registration options found in session"));
            }

            String username = webAuthnService.getCurrentUsername();
            if (username == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "User not authenticated"));
            }

            boolean success = webAuthnService.processRegistration(credential, options, username);
            
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Passkey registered successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Registration failed"));
            }
            
        } catch (Exception e) {
            log.error("Error completing registration", e);
            return ResponseEntity.badRequest().body(Map.of("success", false, "error", "Registration failed"));
        }
    }

    /**
     * パスキー認証用のオプションを生成する
     */
    @PostMapping("/authenticate/options")
    public ResponseEntity<?> getAuthenticationOptions(
            HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            log.info("WebAuthn authentication options requested");
            
            PublicKeyCredentialRequestOptions options = webAuthnService.createAuthenticationOptions();
            
            // セッションにオプションを保存
            sessionRepository.saveRequestOptions(request, response, options);
            
            return ResponseEntity.ok(options);
            
        } catch (Exception e) {
            log.error("Error creating authentication options", e);
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to create authentication options"));
        }
    }
}