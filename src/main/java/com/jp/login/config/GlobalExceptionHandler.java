package com.jp.login.config;

import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

    // HTTPリクエストメソッド不正
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RedirectView handleMethodNotSupported(RedirectAttributes attributes) {
        attributes.addFlashAttribute("errorMessage", "許可されていないリクエスト方法です。");
        return new RedirectView("/userName");
    }

    // その他例外
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}
