package com.example.login.controller;

import com.example.login.mapper.UserMapper;
import com.example.login.service.registerService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class register {

    private final UserMapper userMapper;
    private final registerService registerService;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "login/register";
    }

    @PostMapping("/register")
    public String registerUser(
            @AuthenticationPrincipal com.example.login.security.UserDetails user1,
            @RequestParam("username") String username, @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword, Model model) {

        // 入力値のチェック
        if (!user1.getUsername().equals("admin") || username == null || username.isEmpty()
                || password == null || password.isEmpty() || confirmPassword == null
                || confirmPassword.isEmpty()) {
            model.addAttribute("error", "全てのフィールドに入力してください。");
            return "login/register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            return "login/register";
        }

        // ★ ここで重複チェック（既にユーザー名が使われている場合）
        int existsUser = userMapper.existsById(username);
        if (existsUser > 0) {
            model.addAttribute("error", "「" + username + "」は既に使われています。");
            return "login/register";
        }

        registerService.add(user1.getUsername(), username, password, confirmPassword);

        model.addAttribute("message", "アカウント登録が完了しました");
        model.addAttribute("username", username);
        return "login/login";
    }
}
