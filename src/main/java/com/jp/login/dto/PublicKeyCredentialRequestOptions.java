package com.jp.login.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PublicKeyCredentialRequestOptions {
    private String challenge; // Base64URL encoded
    private Long timeout;
    private String rpId;
    private List<PublicKeyCredentialDescriptor> allowCredentials;
    private String userVerification;

    @Data
    @Builder
    public static class PublicKeyCredentialDescriptor {
        private String type;
        private String id; // Base64URL encoded
        private List<String> transports;
    }
}