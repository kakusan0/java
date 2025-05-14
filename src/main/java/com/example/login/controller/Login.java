package com.example.login.controller;

import com.example.login.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class Login {

    private final UserMapper userMapper;

    @GetMapping("/login")
    public String loginPage(HttpSession session,
            @AuthenticationPrincipal com.example.login.security.UserDetails user) {
        if (user != null) {
            if (userMapper.existsByBindingANDWikiStatus(user.getUsername()) > 0) {
                session.invalidate();
                return "login/login";
            } else if (userMapper.existsByWikiStatus(user.getUsername()) > 0) {
                userMapper.updateWikiStatus(user.getUsername());
                session.invalidate();
                return "login/login";
            }
        }
        return "login/login";
    }
}
