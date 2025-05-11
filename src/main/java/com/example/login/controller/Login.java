package com.example.login.controller;

import com.example.login.form.UserValidation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class Login {

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserValidation form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/login";
        }
        return "redirect:/list";
    }
}
