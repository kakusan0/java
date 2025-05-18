package com.jp.login.controller;

import com.jp.login.entity.user;
import com.jp.login.mapper.UserMapper;
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
    public String userName() {
        return "login/userName";
    }

    @PostMapping("/userNameCheck")
    public String userNameCheck(Model model,
            @ModelAttribute("userValidation") user user
            ) {
        if (ObjectUtils.isEmpty(user)) {
            String errorMsg = messageSource.getMessage(
                    "login.error.duplicate", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "login/userName";
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
