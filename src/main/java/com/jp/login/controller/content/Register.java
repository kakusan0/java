package com.jp.login.controller.content;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.login.constants.ApplicationConstants;
import com.jp.login.mapper.UserMapper;
import com.jp.login.service.registerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class Register {

    private final UserMapper userMapper;
    private final registerService registerService;

    @GetMapping(ApplicationConstants.REGISTER)
    public String register() {
        return "login/register";
    }

    @PostMapping(ApplicationConstants.REGISTER)
    public String registerUser(@RequestParam String username, Model model) {
        if (!StringUtils.hasText(username)) {
            model.addAttribute("error", "ユーザ名を入力してください。");
            return "login/register";
        }

        if (userMapper.existsById(username) > 0) {
            model.addAttribute("error", "「" + username + "」は既に使われています。");
            return "login/register";
        }

        String password = registerService.insert(username);
        model.addAttribute("message", "アカウント登録が完了しました");
        model.addAttribute("username", username);
        model.addAttribute("password", password);
        return "login/register";
    }
}
