package com.jp.login.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class PublicKeyCredentialCreationOptions {
    private RelyingParty rp;
    private PublicKeyCredentialUserEntity user;
    private String challenge; // Base64URL encoded
    private List<PublicKeyCredentialParameters> pubKeyCredParams;
    private Long timeout;
    private List<PublicKeyCredentialDescriptor> excludeCredentials;
    private AuthenticatorSelectionCriteria authenticatorSelection;
    private String attestation;

    @Data
    @Builder
    public static class RelyingParty {
        private String id;
        private String name;
    }

    @Data
    @Builder  
    public static class PublicKeyCredentialUserEntity {
        private String id; // Base64URL encoded
        private String name;
        private String displayName;
    }

    @Data
    @Builder
    public static class PublicKeyCredentialParameters {
        private String type;
        private Integer alg;
    }

    @Data
    @Builder
    public static class PublicKeyCredentialDescriptor {
        private String type;
        private String id; // Base64URL encoded
        private List<String> transports;
    }

    @Data
    @Builder
    public static class AuthenticatorSelectionCriteria {
        private String authenticatorAttachment;
        private String userVerification;
    }
}