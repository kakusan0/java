package com.jp.login.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class FormLoginMethodCheckFilter extends OncePerRequestFilter {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    // URLパターンとアクセス制御のマッピング
    private static final Map<String, Boolean> URL_PATTERNS = Map.of(
            "/login/**", true,      // ログイン関連
            "/register", true,      // 登録ページ
            "/css/**", true,       // 静的リソース
            "/js/**", true,
            "/img/**", true,
            "/webjars/**", true,
            "/userName", true,      // ユーザー名ページ
            "/userNameCheck", true  // ユーザー名チェック
    );

    @Override
    protected void doFilterInternal(
            @NonNull
            HttpServletRequest request,
            @NonNull
            HttpServletResponse response,
            @NonNull
            FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        String method = request.getMethod();

        // GETメソッドでのログインURLへのアクセスをチェック
        if (HttpMethod.GET.matches(method) && pathMatcher.match("/login/**", requestUri)) {
            response.sendRedirect("/userName");
            return;
        }

        // パターンマッチングの確認
        boolean isPermitted = URL_PATTERNS.entrySet().stream()
                .anyMatch(entry -> pathMatcher.match(entry.getKey(), requestUri));

        if (!isPermitted) {
            response.sendRedirect("/userName");
            return;
        }

        filterChain.doFilter(request, response);
    }
}