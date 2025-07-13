package com.jp.login.controller;

import com.jp.login.entity.MasterUser;
import com.jp.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class UserCheckController {

    private final UserMapper userMapper;
    private final MessageSource messageSource;

    @PostMapping("/userNameCheck")
    public String userNameCheck(Model model,
            @ModelAttribute("userValidation") MasterUser user
            ) {
        if (ObjectUtils.isEmpty(user)) {
            String errorMsg = messageSource.getMessage(
                    "login.error.duplicate", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "login/userName";
        }
        if (userMapper.existsById(user.getUsername()) == 0) {
            String errorMsg = messageSource.getMessage(
                    "login.error.notfound", null, Locale.getDefault());
            model.addAttribute("error", user.getUsername() + errorMsg);
            return "redirect:/userName";
        }
        model.addAttribute("username", user.getUsername());
        log.debug("userName: {}", user.getUsername());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<SimpleGrantedAuthority> updatedAuthorities =
                authentication.getAuthorities()
                        .stream()
                        .map(a -> new SimpleGrantedAuthority(a.getAuthority()))
                        .collect(Collectors.toList());
        updatedAuthorities.add(new SimpleGrantedAuthority("ROLE_UserCheckOK"));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                authentication.getPrincipal(),
                authentication.getCredentials(),
                updatedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "login/login";
    }
}
