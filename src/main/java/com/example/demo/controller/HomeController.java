package com.example.demo.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final String USERNAME = "username";
    private static final String ID = "id";
    private static final String AVATAR = "avatar";


    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String home(OAuth2AuthenticationToken authentication, Model model) {
        model.addAttribute("name", extractAttribute(authentication, USERNAME));
        model.addAttribute("id", extractAttribute(authentication, ID));
        model.addAttribute("avatar", extractAttribute(authentication, AVATAR));
        System.out.println(authentication.getPrincipal().getAttributes());
        return "home";
    }

    private String extractAttribute(OAuth2AuthenticationToken authentication, String attribute) {
        return (String) authentication.getPrincipal().getAttributes().get(attribute);
    }
}