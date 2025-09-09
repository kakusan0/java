package com.jp.login.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.lang.NonNull;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jp.login.constants.ApplicationConstants;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class FormLoginMethodCheckFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // URLパターンとアクセス制御のマッピング
    private static final Map<String, Boolean> URL_PATTERNS = Map.of(
            ApplicationConstants.LOGIN, true, // ログイン関連
            ApplicationConstants.REGISTER, true, // 登録ページ
            ApplicationConstants.MAIN, true,
            ApplicationConstants.USER_CHECK, true, // ユーザー名ページ
            ApplicationConstants.USERNAME_CHECK, true, // ユーザー名チェック
            "/css/**", true, // 静的リソース
            "/js/**", true,
            "/img/**", true,
            "/webjars/**", true
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        // GETメソッドでのログインURLへのアクセスをチェック
        if (HttpMethod.GET.matches(method) && pathMatcher.match("/login", requestUri)) {
            response.sendRedirect(ApplicationConstants.USERNAME_CHECK);
            return;
        }

        // パターンマッチングの確認
        boolean isPermitted = URL_PATTERNS.entrySet().stream()
                .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestUri));

        if (!isPermitted) {
            response.sendRedirect(ApplicationConstants.USERNAME_CHECK);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
