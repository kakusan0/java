package com.jp.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.jp.login.constants.ApplicationConstants.ApplicationBase.*;
import static com.jp.login.constants.ApplicationConstants.ApplicationRedirectUrl.from_userName_redirect;
import static com.jp.login.constants.ApplicationConstants.ApplicationToUrl.login_to_register;
import static com.jp.login.constants.ApplicationConstants.ApplicationToUrl.login_to_userName;

@Controller
public class RootController {

    @GetMapping(ROOT)
    public String root() {
        return from_userName_redirect;
    }
    
    @GetMapping(USERNAME)
    public String userName() {
        return login_to_userName;
    }

    @GetMapping(register)
    public String register() {
        return login_to_register;
    }
}
