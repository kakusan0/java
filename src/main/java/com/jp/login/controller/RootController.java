package com.jp.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.login.constants.ApplicationConstants;
import com.jp.login.entity.ContentItemExample;
import com.jp.login.mapper.ContentItemMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RootController {
    private final ContentItemMapper contentItemMapper;

    @GetMapping(ApplicationConstants.ROOT)
    public String root() {
        // redirect to the username entry page
        return ApplicationConstants.REDIRECT + ApplicationConstants.USERNAME_CHECK;
    }

    @GetMapping(ApplicationConstants.USERNAME_CHECK)
    public String userName() {
        // render templates/login/userName.html
        return "login/userName";
    }

    @GetMapping(ApplicationConstants.USER_CHECK)
    public String userCheck() {
        // render templates/login/userCheck.html
        return "login/userCheck";
    }

    @GetMapping(ApplicationConstants.REGISTER)
    public String register() {
        // render templates/login/register.html
        return "login/register";
    }

    @GetMapping(ApplicationConstants.MAIN)
    public String root(Model model) {
        model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));
        return ApplicationConstants.MAIN;
    }

    @PostMapping(ApplicationConstants.CONTENT)
    public String selectItem(
            @RequestParam(defaultValue = "未選択") String screenName,
            Model model) {
        model.addAttribute("errorMessage", null);
        model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));

        // "未選択"の場合には caseにヒットしない値を渡す
        model.addAttribute("currentScreen", "未選択".equals(screenName) ? "" : screenName);

        model.addAttribute("selectedScreenName", "未選択".equals(screenName) ? "画面を選択" : screenName);
        return ApplicationConstants.MAIN;
    }
}
