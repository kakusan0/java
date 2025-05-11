package com.example.login.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;

@Controller
@PreAuthorize("hasRole('USER')")
public class wiki {

//    @Value("${RCON_HOSTNAME}")
//    private String redirectURL;

    @GetMapping("/public")
    public String home(@AuthenticationPrincipal com.example.login.security.UserDetails user) {
        String url = "https://wiki.aristos.server-on.net/";
        if (user != null && !ObjectUtils.isEmpty(user)) {
            url += "?user=" + encode(String.valueOf(user.getUsername()), StandardCharsets.UTF_8);
        }
        return "redirect:" + url;
    }
}
