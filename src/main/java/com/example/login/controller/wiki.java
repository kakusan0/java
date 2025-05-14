package com.example.login.controller;

import com.example.login.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class wiki {

    private final UserMapper userMapper;

    @Value("${wiki.url}")
    private String redirectURL;

    @GetMapping("/public")
    public String home(@AuthenticationPrincipal com.example.login.security.UserDetails user,
            Model model, HttpSession session) {
        if (user != null && !ObjectUtils.isEmpty(user)) {
            redirectURL +=
                    "?user=" + encode(String.valueOf(user.getUsername()), StandardCharsets.UTF_8);
        }
        if (user != null) {
            if (userMapper.existsByBindingANDWikiStatus(user.getUsername()) > 0) {
                session.invalidate();
                model.addAttribute("error", "管理者にお問い合わせください");
                return "login/login";
            }
        }
        return "redirect:" + redirectURL;
    }
}
