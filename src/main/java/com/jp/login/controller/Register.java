package com.jp.login.controller;

import static com.jp.login.constants.ApplicationConstants.ApplicationBase.register;
import static com.jp.login.constants.ApplicationConstants.ApplicationToUrl.login_to_register;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.login.mapper.UserMapper;
import com.jp.login.service.registerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class Register {

    private final UserMapper userMapper;
    private final registerService registerService;

    @PostMapping(register)
    public String registerUser(@RequestParam String username, Model model) {
        // 入力値のチェック
        if (username == null || username.isEmpty()) {
            model.addAttribute("error", "ユーザ名を入力してください。");
            return login_to_register;
        }

        // ★ ここで重複チェック（既にユーザー名が使われている場合）
        int existsUser = userMapper.existsById(username);
        if (existsUser > 0) {
            model.addAttribute("error", "「" + username + "」は既に使われています。");
            return login_to_register;
        }

        String password = registerService.insert(username);

        model.addAttribute("message", "アカウント登録が完了しました");
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        return login_to_register;
    }
}
