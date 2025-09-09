package com.jp.login.controller.content;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jp.login.constants.ApplicationConstants;
import com.jp.login.entity.ContentItemExample;
import com.jp.login.mapper.ContentItemMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
public class ContentItemSelectList {
    private final ContentItemMapper contentItemMapper;

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
