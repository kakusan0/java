package com.example.login.controller;

import com.example.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
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

//    @Value("${RCON_HOSTNAME}")
//    private String redirectURL;

    @GetMapping("/public")
    public String home(@AuthenticationPrincipal com.example.login.security.UserDetails user, Model model) {
        String url = "https://wiki.aristos.server-on.net/";
        if (user != null && !ObjectUtils.isEmpty(user)) {
            url += "?user=" + encode(String.valueOf(user.getUsername()), StandardCharsets.UTF_8);
        }
        if (user != null) {
            if (userMapper.existsByBindingANDWikiStatus(user.getUsername()) > 0) {
                model.addAttribute("error", "管理者にお問い合わせください");
                return "login/login";
            }
        }
        return "redirect:" + url;
    }
}
