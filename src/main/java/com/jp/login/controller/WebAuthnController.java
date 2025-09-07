package com.jp.login.controller;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// 簡易DTO定義（本番は別ファイル推奨）
class PublicKeyCredentialDescriptor {
    public String type = "public-key";
    public String id; // base64url
    public List<String> transports = List.of("internal", "hybrid", "usb", "nfc", "ble");
}

class PublicKeyCredentialRequestOptions {
    public String challenge; // base64url
    public Long timeout;     // ms
    public String rpId;
    public List<PublicKeyCredentialDescriptor> allowCredentials = Collections.emptyList();
    public String userVerification = "preferred";
}

class AuthenticatorAssertionResponseDTO {
    public String id;
    public String type;
    public String rawId;
    public Response response = new Response();
    public Object clientExtensionResults;

    public static class Response {
        public String authenticatorData;
        public String clientDataJSON;
        public String signature;
        public String userHandle; // null 可
    }
}

@RestController
@RequestMapping("/webauthn")
public class WebAuthnController {

    private static final String SESSION_CHALLENGE_KEY = "webauthn.challenge";
    private static final Base64.Encoder B64URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder B64URL_DECODER = Base64.getUrlDecoder();
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @PostMapping("/authentication/options")
    public ResponseEntity<?> authenticationOptions(HttpServletRequest request) {
        // 1) チャレンジ生成（32バイト推奨）
        byte[] challenge = new byte[32];
        RANDOM.nextBytes(challenge);

        // 2) セッションに保存（検証時に突合）
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_CHALLENGE_KEY, challenge);

        // 3) rpId はホスト名（本番は固定推奨）
        String rpId = request.getServerName();

        // 4) publicKey オプションをネストして返す（フロントの期待形）
        Map<String, Object> publicKey = new HashMap<>();
        publicKey.put("challenge", B64URL_ENCODER.encodeToString(challenge));
        publicKey.put("timeout", Duration.ofSeconds(60).toMillis());
        publicKey.put("rpId", rpId);
        publicKey.put("userVerification", "preferred");
        publicKey.put("allowCredentials", Collections.emptyList()); // 必要に応じて credentialId を詰める

        Map<String, Object> body = Map.of("publicKey", publicKey);

        return ResponseEntity.ok(body);
    }


    @PostMapping("/authentication/verify")
    public ResponseEntity<Void> authenticationVerify(
        @RequestBody AuthenticatorAssertionResponseDTO assertion,
        HttpServletRequest request) {
        // セッションからチャレンジ取得
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SESSION_CHALLENGE_KEY) == null) {
            return ResponseEntity.badRequest().build();
        }
        byte[] expectedChallenge = (byte[]) session.getAttribute(SESSION_CHALLENGE_KEY);
        session.removeAttribute(SESSION_CHALLENGE_KEY); // 使い捨て

        try {
            // 最低限の検証: clientDataJSON の challenge と type を確認
            byte[] clientDataBytes = B64URL_DECODER.decode(assertion.response.clientDataJSON);
            JsonNode clientData = MAPPER.readTree(clientDataBytes);

            String type = clientData.path("type").asText();
            if (!"webauthn.get".equals(type)) {
                return ResponseEntity.badRequest().build();
            }
            String challengeB64url = clientData.path("challenge").asText();
            byte[] challengeFromClient = B64URL_DECODER.decode(challengeB64url);
            if (!constantTimeEquals(expectedChallenge, challengeFromClient)) {
                return ResponseEntity.badRequest().build();
            }

            // 本来ここで:
            // - origin 検証（clientData.origin）
            // - authenticatorData/rpIdHash/flags/signCount の検証
            // - publicKey で signature 検証（DBに保持の公開鍵）
            // を行うこと

            // 簡易サンプル: ここでアプリ内のログインを成立させる
            // 実ユーザーの特定は credentialId(assertion.id/rawId) や userHandle からDB照合して行う
            String principal = "passkey-user"; // 実装時はDBからユーザー名を引く
            var authorities = List.of(
                new SimpleGrantedAuthority("ROLE_UserCheckOK"),
                new SimpleGrantedAuthority("ROLE_USER")
            );
            var authentication = new UsernamePasswordAuthenticationToken(principal, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private static boolean constantTimeEquals(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) return false;
        int res = 0;
        for (int i = 0; i < a.length; i++) res |= a[i] ^ b[i];
        return res == 0;
    }
}
