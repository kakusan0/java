package com.jp.login.controller;

import static com.jp.login.constants.ApplicationConstants.ApplicationBase.ROOT;
import static com.jp.login.constants.ApplicationConstants.ApplicationBase.USERNAME;
import static com.jp.login.constants.ApplicationConstants.ApplicationBase.register;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.login.entity.ContentItemExample;
import com.jp.login.mapper.ContentItemMapper;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@Profile("dev")
public class RootController {
    private final ContentItemMapper contentItemMapper;

    @GetMapping(ROOT)
    public String root() {
        // redirect to the username entry page
        return "redirect:/userName";
    }

    @GetMapping(USERNAME)
    public String userName() {
        // render templates/login/userName.html
        return "login/userName";
    }

    @GetMapping(register)
    public String register() {
        // render templates/login/register.html
        return "login/register";
    }

    @GetMapping("/main")
    public String root(Model model) {
        model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));
        return "main";
    }

    @PostMapping("/content")
    public String selectItem(
            @RequestParam(defaultValue = "未選択") String screenName,
            Model model) {
        model.addAttribute("errorMessage", null);
        model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));

        // "未選択"の場合には caseにヒットしない値を渡す
        model.addAttribute("currentScreen", "未選択".equals(screenName) ? "" : screenName);

        model.addAttribute("selectedScreenName", "未選択".equals(screenName) ? "画面を選択" : screenName);
        return "main";
    }
}

// @Controller
// @RequiredArgsConstructor
// @Profile("!dev")
// class Dev1Controller {

//     @GetMapping(ROOT)
//     public String root(Model model) {
//         model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));
//         return "main";
//     }

//     @PostMapping("/content")
//     public String selectItem(
//             @RequestParam(defaultValue = "未選択") String screenName,
//             Model model) {
//         model.addAttribute("errorMessage", null);
//         model.addAttribute("screens", contentItemMapper.selectByExample(new ContentItemExample()));

//         // "未選択"の場合には caseにヒットしない値を渡す
//         model.addAttribute("currentScreen", "未選択".equals(screenName) ? "" : screenName);

//         model.addAttribute("selectedScreenName", "未選択".equals(screenName) ? "画面を選択" : screenName);
//         return "main";
//     }
// }
