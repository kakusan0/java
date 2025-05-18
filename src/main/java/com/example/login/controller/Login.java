package com.example.login.controller;

import com.example.login.entity.MasterUser;
import com.example.login.form.UserValidation;
import com.example.login.mapper.UserMapper;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/userName")
    public String userName() {
        return "login/userNameCheack";
    }

    @PostMapping("/userNameCheck")
    public String userNameCheck(
            Model model,
            @RequestParam(name = "username") @Validated UserValidation userName,
            BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userMapper.existsById1(userName);
            model.addAttribute("username", userName);
        } else {
            model.addAttribute("error", userName + "はすでに使用されています");
        }
        return "login/login";
    }
}
