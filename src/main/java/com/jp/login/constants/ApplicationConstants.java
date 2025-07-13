package com.jp.login.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationConstants {
    public static final String SLASH = "/";
    public static final String REDIRECT = "redirect:";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class RegisterConstants {
        public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        public static final int PASSWORD_LENGTH = 15;
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class ApplicationBase {
        public static final String ROOT = "/";
        public static final String USERNAME = "userName";
        public static final String userNameCheck = "userNameCheck";
        public static final String login = "login";
        public static final String register = "register";
    }

    public static final class ApplicationToUrl {
        public static final String login_to_register = ApplicationBase.login + SLASH + ApplicationBase.register;
        public static final String login_to_userName = ApplicationBase.login + SLASH + ApplicationBase.USERNAME;
    }

    public static final class ApplicationFromUrl {
        public static final String from_login = ApplicationBase.login + SLASH + ApplicationBase.login;
    }

        public static final class ApplicationRedirectUrl {
        public static final String from_userName_redirect = REDIRECT + SLASH + ApplicationBase.USERNAME;
    }
}
