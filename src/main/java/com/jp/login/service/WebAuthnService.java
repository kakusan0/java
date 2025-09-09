package com.jp.login.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import com.jp.login.dto.PublicKeyCredentialCreationOptions;
import com.jp.login.dto.PublicKeyCredentialRequestOptions;
import com.jp.login.entity.MstUser;
import com.jp.login.entity.MstUserExample;
import com.jp.login.entity.WebauthnCredentials;
import com.jp.login.entity.WebauthnCredentialsWithBLOBs;
import com.jp.login.entity.WebauthnCredentialsExample;
import com.jp.login.mapper.MstUserMapper;
import com.jp.login.mapper.WebauthnCredentialsMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebAuthnService {

    private final MstUserMapper userMapper;
    private final WebauthnCredentialsMapper credentialsMapper;
    private final SecureRandom secureRandom = new SecureRandom();

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
            return auth.getName();
        }
        return null;
    }

    public PublicKeyCredentialCreationOptions createRegistrationOptions(String username) {
        // ユーザー情報を取得
        MstUserExample example = new MstUserExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<MstUser> users = userMapper.selectByExample(example);
        
        if (users.isEmpty()) {
            throw new RuntimeException("User not found: " + username);
        }
        
        MstUser user = users.get(0);
        
        // ユーザーハンドルを生成（既存の認証情報から取得するか新規生成）
        String userHandle = Base64.getUrlEncoder().withoutPadding().encodeToString(generateUserHandle(username));
        
        // 既存の認証情報を取得してexcludeCredentialsに設定
        List<PublicKeyCredentialCreationOptions.PublicKeyCredentialDescriptor> excludeCredentials = getExistingCredentials(username);

        return PublicKeyCredentialCreationOptions.builder()
                .rp(PublicKeyCredentialCreationOptions.RelyingParty.builder()
                        .id("localhost")
                        .name("Local RP")
                        .build())
                .user(PublicKeyCredentialCreationOptions.PublicKeyCredentialUserEntity.builder()
                        .id(userHandle)
                        .name(username)
                        .displayName(username)
                        .build())
                .challenge(Base64.getUrlEncoder().withoutPadding().encodeToString(generateChallenge()))
                .pubKeyCredParams(Arrays.asList(
                        PublicKeyCredentialCreationOptions.PublicKeyCredentialParameters.builder()
                                .type("public-key")
                                .alg(-7) // ES256
                                .build(),
                        PublicKeyCredentialCreationOptions.PublicKeyCredentialParameters.builder()
                                .type("public-key")
                                .alg(-257) // RS256
                                .build()
                ))
                .timeout(60000L)
                .excludeCredentials(excludeCredentials)
                .authenticatorSelection(PublicKeyCredentialCreationOptions.AuthenticatorSelectionCriteria.builder()
                        .authenticatorAttachment("cross-platform")
                        .userVerification("preferred")
                        .build())
                .attestation("none")
                .build();
    }

    public PublicKeyCredentialRequestOptions createAuthenticationOptions() {
        // 全ユーザーの認証情報を取得してallowCredentialsに設定
        List<PublicKeyCredentialRequestOptions.PublicKeyCredentialDescriptor> allowCredentials = getAllCredentials();

        return PublicKeyCredentialRequestOptions.builder()
                .challenge(Base64.getUrlEncoder().withoutPadding().encodeToString(generateChallenge()))
                .timeout(60000L)
                .rpId("localhost")
                .allowCredentials(allowCredentials)
                .userVerification("preferred")
                .build();
    }

    public boolean processRegistration(Map<String, Object> credential, PublicKeyCredentialCreationOptions options, String username) {
        try {
            // credentialの構造を解析
            @SuppressWarnings("unchecked")
            Map<String, Object> publicKey = (Map<String, Object>) credential.get("publicKey");
            if (publicKey == null) {
                log.error("No publicKey found in credential");
                return false;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> credentialData = (Map<String, Object>) publicKey.get("credential");
            if (credentialData == null) {
                log.error("No credential data found");
                return false;
            }

            // 必要な値を抽出
            String credentialIdStr = (String) credentialData.get("id");
            String rawIdStr = (String) credentialData.get("rawId");
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = (Map<String, Object>) credentialData.get("response");
            if (response == null) {
                log.error("No response found in credential");
                return false;
            }

            String clientDataJSON = (String) response.get("clientDataJSON");
            String attestationObject = (String) response.get("attestationObject");

            // Base64デコード
            byte[] credentialId = Base64.getUrlDecoder().decode(credentialIdStr);
            byte[] publicKeyCose = Base64.getUrlDecoder().decode(attestationObject);
            
            // ユーザーハンドルを生成
            byte[] userHandle = generateUserHandle(username);

            // データベースに保存
            WebauthnCredentialsWithBLOBs webauthnCreds = new WebauthnCredentialsWithBLOBs();
            webauthnCreds.setCredentialId(credentialId);
            webauthnCreds.setUsername(username);
            webauthnCreds.setPublicKeyCose(publicKeyCose);
            webauthnCreds.setUserHandle(userHandle);
            webauthnCreds.setSignCount(0L);

            int result = credentialsMapper.insert(webauthnCreds);
            
            log.info("Passkey registration completed for user: {}, result: {}", username, result);
            return result > 0;
            
        } catch (Exception e) {
            log.error("Error processing registration", e);
            return false;
        }
    }

    public String processAuthentication(Map<String, Object> credential, PublicKeyCredentialRequestOptions options, 
                                      HttpServletRequest request, HttpServletResponse response) {
        try {
            // 認証情報からcredential IDを抽出
            String credentialIdStr = (String) credential.get("id");
            if (credentialIdStr == null) {
                log.error("No credential ID found");
                return null;
            }

            byte[] credentialId = Base64.getUrlDecoder().decode(credentialIdStr);

            // データベースから認証情報を検索
            WebauthnCredentialsWithBLOBs storedCreds = credentialsMapper.selectByPrimaryKey(credentialId);
            if (storedCreds == null) {
                log.error("No stored credentials found for credential ID");
                return null;
            }

            String username = storedCreds.getUsername();
            
            // ユーザー情報を取得
            MstUserExample example = new MstUserExample();
            example.createCriteria().andUsernameEqualTo(username);
            List<MstUser> users = userMapper.selectByExample(example);
            
            if (users.isEmpty()) {
                log.error("User not found: {}", username);
                return null;
            }
            
            MstUser user = users.get(0);
            
            // Spring Securityのコンテキストに認証情報を設定
            List<GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority(user.getRole()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // セッションに認証情報を保存
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, 
                                            SecurityContextHolder.getContext());
            
            log.info("User {} authenticated successfully with passkey", username);
            return username;
            
        } catch (Exception e) {
            log.error("Error processing authentication", e);
            return null;
        }
    }

    private byte[] generateChallenge() {
        byte[] challenge = new byte[32];
        secureRandom.nextBytes(challenge);
        return challenge;
    }

    private byte[] generateUserHandle(String username) {
        // 既存のユーザーハンドルをチェック
        WebauthnCredentialsExample example = new WebauthnCredentialsExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<WebauthnCredentials> existing = credentialsMapper.selectByExample(example);
        
        if (!existing.isEmpty()) {
            // 既存のユーザーハンドルを使用
            WebauthnCredentialsWithBLOBs first = credentialsMapper.selectByPrimaryKey(existing.get(0).getCredentialId());
            return first.getUserHandle();
        }
        
        // 新規生成
        byte[] userHandle = new byte[32];
        secureRandom.nextBytes(userHandle);
        return userHandle;
    }

    private List<PublicKeyCredentialCreationOptions.PublicKeyCredentialDescriptor> getExistingCredentials(String username) {
        WebauthnCredentialsExample example = new WebauthnCredentialsExample();
        example.createCriteria().andUsernameEqualTo(username);
        List<WebauthnCredentials> credentials = credentialsMapper.selectByExample(example);
        
        return credentials.stream()
                .map(cred -> PublicKeyCredentialCreationOptions.PublicKeyCredentialDescriptor.builder()
                        .type("public-key")
                        .id(Base64.getUrlEncoder().withoutPadding().encodeToString(cred.getCredentialId()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<PublicKeyCredentialRequestOptions.PublicKeyCredentialDescriptor> getAllCredentials() {
        List<WebauthnCredentials> credentials = credentialsMapper.selectByExample(new WebauthnCredentialsExample());
        
        return credentials.stream()
                .map(cred -> PublicKeyCredentialRequestOptions.PublicKeyCredentialDescriptor.builder()
                        .type("public-key")
                        .id(Base64.getUrlEncoder().withoutPadding().encodeToString(cred.getCredentialId()))
                        .build())
                .collect(Collectors.toList());
    }
}