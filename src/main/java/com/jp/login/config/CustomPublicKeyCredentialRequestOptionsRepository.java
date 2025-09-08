// java
package com.jp.login.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.web.webauthn.api.PublicKeyCredentialRequestOptions;
import org.springframework.security.web.webauthn.authentication.PublicKeyCredentialRequestOptionsRepository;
import org.springframework.util.Assert;

/**
 * Passkey 認証用の PublicKeyCredentialRequestOptions をセッションへ保存する実装。
 */
public class CustomPublicKeyCredentialRequestOptionsRepository
        implements PublicKeyCredentialRequestOptionsRepository {

    public static final String SESSION_ATTRIBUTE_NAME = "PASSKEY_AUTHENTICATION_OPTIONS";

    @Override
    public void save(HttpServletRequest request, HttpServletResponse response, PublicKeyCredentialRequestOptions options) {
        Assert.notNull(request, "request cannot be null");
        Assert.notNull(options, "options cannot be null");
        HttpSession session = request.getSession(true);
        session.setAttribute(SESSION_ATTRIBUTE_NAME, options);
    }

    @Override
    public PublicKeyCredentialRequestOptions load(HttpServletRequest request) {
        Assert.notNull(request, "request cannot be null");
        HttpSession session = request.getSession(false);
        return (session != null)
                ? (PublicKeyCredentialRequestOptions) session.getAttribute(SESSION_ATTRIBUTE_NAME)
                : null;
    }
}
