package com.example.login.controller;

import com.example.login.entity.MasterUser;
import com.example.login.form.formUser;
import com.example.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Locale;

@Controller
@RequiredArgsConstructor
@Log4j2
public class Login {

    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @GetMapping("/userName")
    public String userName(@ModelAttribute formUser formUser) {
        return "login/userName";
    }

    @PostMapping("/userNameCheck")
    public String userNameCheck(
            Model model,
            @ModelAttribute("userValidation") MasterUser user
            ) {
        if (ObjectUtils.isEmpty(user)) {
            String errorMsg = messageSource.getMessage(
                    "login.error.duplicate", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "login/login";
        }
        int count = userMapper.existsById(user.getUsername());
        if (count == 0) {
            String errorMsg = messageSource.getMessage(
                    "login.error.notfound", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "login/userName";
        }
        model.addAttribute("username", user.getUsername());
        log.debug("userName: {}", user.getUsername());
        return "login/login";
    }
}
