package com.jp.login.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class formUser {

    @NotBlank(message = "ユーザー名は必須です")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上必要です")
    private String password;

    // ... getter and setter
}
