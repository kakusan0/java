package com.jp.login.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
// ...existing imports...
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.jp.login.constants.ApplicationConstants;
import com.jp.login.entity.MasterUser;
import com.jp.login.mapper.UserMapper;
import com.jp.login.service.SecurityAuthorityService;

import jakarta.validation.Valid;
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
    public String userNameCheck(Model model, @ModelAttribute("userValidation") @Valid MasterUser user,
            BindingResult bindingResult) {
        // validation errors from @NotBlank will populate bindingResult
        if (bindingResult.hasErrors()) {
            String err = bindingResult.getFieldErrors().stream().findFirst()
                    .map(fe -> messageSource.getMessage(fe.getDefaultMessage(), null, Locale.getDefault()))
                    .orElse(messageSource.getMessage("login.error.login", null, Locale.getDefault()));
            model.addAttribute("error", err);
            return "login/login";
        }

        String username = user.getUsername();
        if (userMapper.existsById(username) == 0) {
            String errorMsg = messageSource.getMessage("login.error.notfound", null, Locale.getDefault());
            model.addAttribute("error", username + errorMsg);
            return "login/login";
        }

        model.addAttribute("username", username);
        log.debug("userName: {}", username);
        securityAuthorityService.addAuthority("ROLE_UserCheckOK");
        return "login/login";
    }

}
