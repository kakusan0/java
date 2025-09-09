package com.jp.login.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jp.login.constants.ApplicationConstants;
import com.jp.login.entity.MasterUser;
import com.jp.login.mapper.UserMapper;
import com.jp.login.service.SecurityAuthorityService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class RootController {
    private final UserMapper userMapper;
    private final MessageSource messageSource;
    private final SecurityAuthorityService securityAuthorityService;

    @GetMapping(ApplicationConstants.ROOT)
    public String root() {
        // redirect to the username entry page
        return ApplicationConstants.REDIRECT + ApplicationConstants.USERNAME_CHECK;
    }

    @GetMapping(ApplicationConstants.USERNAME_CHECK)
    public String userName() {
        // render templates/login/userName.html
        return "login/login";
    }

    @GetMapping(ApplicationConstants.USER_CHECK)
    public String userCheck() {
        // render templates/login/userCheck.html
        return "login/login";
    }

    @PostMapping(ApplicationConstants.USER_CHECK)
    public String userNameCheck(Model model,
            @ModelAttribute("userValidation") MasterUser user) {
        if (ObjectUtils.isEmpty(user)) {
            String errorMsg = messageSource.getMessage(
                    "login.error.duplicate", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "login/login";
        }
        if (userMapper.existsById(user.getUsername()) == 0) {
            String errorMsg = messageSource.getMessage(
                    "login.error.notfound", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return ApplicationConstants.REDIRECT + ApplicationConstants.USERNAME_CHECK;
        }
        model.addAttribute("username", user.getUsername());
        log.debug("userName: {}", user.getUsername());
        securityAuthorityService.addAuthority("ROLE_UserCheckOK");
        return "login/login";
    }


}
