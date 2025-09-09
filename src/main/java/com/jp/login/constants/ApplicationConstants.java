package com.jp.login.constants;

public class ApplicationConstants {
    public static final String SLASH = "/";
    public static final String REDIRECT = "redirect:";

    public static final class RegisterConstants {
        public static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        public static final int PASSWORD_LENGTH = 15;
    }

    // 基本パス
    public static final String ROOT = "/";
    public static final String USERNAME = "userName";
    public static final String USERNAME_CHECK = "userNameCheck";
    public static final String LOGIN = "login";
    public static final String REGISTER = "register";

    // 互換性を保つため既存のネーミングを残すエイリアス
    public static final class ApplicationBase {
        public static final String ROOT = ApplicationConstants.ROOT;
        public static final String USERNAME = ApplicationConstants.USERNAME;
        public static final String userNameCheck = ApplicationConstants.USERNAME_CHECK;
        public static final String LOGIN = ApplicationConstants.LOGIN;
        public static final String register = ApplicationConstants.REGISTER;
    }

    // 備考: view 名や redirect はコントローラ側で直接返すようにするため、
    // 組み合わせ用の URL 定数は削除しました。
}
