package com.jp.login.service;

import org.springframework.stereotype.Component;

import com.jp.login.dto.PublicKeyCredentialCreationOptions;
import com.jp.login.dto.PublicKeyCredentialRequestOptions;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SimpleWebAuthnSessionRepository {

    public static final String CREATION_OPTIONS_SESSION_ATTRIBUTE = "WEBAUTHN_CREATION_OPTIONS";
    public static final String REQUEST_OPTIONS_SESSION_ATTRIBUTE = "WEBAUTHN_REQUEST_OPTIONS";

    public void saveCreationOptions(HttpServletRequest request, HttpServletResponse response, 
                                   PublicKeyCredentialCreationOptions options) {
        HttpSession session = request.getSession(true);
        session.setAttribute(CREATION_OPTIONS_SESSION_ATTRIBUTE, options);
    }

    public PublicKeyCredentialCreationOptions loadCreationOptions(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) 
            ? (PublicKeyCredentialCreationOptions) session.getAttribute(CREATION_OPTIONS_SESSION_ATTRIBUTE)
            : null;
    }

    public void saveRequestOptions(HttpServletRequest request, HttpServletResponse response, 
                                  PublicKeyCredentialRequestOptions options) {
        HttpSession session = request.getSession(true);
        session.setAttribute(REQUEST_OPTIONS_SESSION_ATTRIBUTE, options);
    }

    public PublicKeyCredentialRequestOptions loadRequestOptions(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return (session != null) 
            ? (PublicKeyCredentialRequestOptions) session.getAttribute(REQUEST_OPTIONS_SESSION_ATTRIBUTE)
            : null;
    }
}