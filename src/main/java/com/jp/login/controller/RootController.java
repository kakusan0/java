package com.jp.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:/userName";
    }
    
    @GetMapping("/userName")
    public String userName() {
        return "login/userName";
    }

    @GetMapping("/register")
    public String register() {
        return "login/register";
    }
}
