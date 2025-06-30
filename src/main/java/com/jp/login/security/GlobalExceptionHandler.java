package com.jp.login.security;

// GlobalExceptionHandler.java

import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.view.RedirectView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RedirectView handleMethodNotSupported() {
        // 必要に応じてフラッシュ属性など追加可能
        return new RedirectView("/userName");
    }
}