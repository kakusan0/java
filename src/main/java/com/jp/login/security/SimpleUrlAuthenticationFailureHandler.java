package com.jp.login.security;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleUrlAuthenticationFailureHandler
        extends org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        String username = request.getParameter("username");
        getRedirectStrategy().sendRedirect(request, response, "/userNameCheck?error&username=" + username);
    }
}