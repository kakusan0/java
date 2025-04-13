package com.example.demo.controller.login;

import com.example.demo.entity.post.MasterUser;
import com.example.demo.mapper.post.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class Login {

    private final UserMapper userMapper;

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "login/register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam("username") String username,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model) {

        // 入力値のチェック
        if (username == null || username.isEmpty() ||
                email == null || email.isEmpty() ||
                password == null || password.isEmpty() ||
                confirmPassword == null || confirmPassword.isEmpty()) {
            model.addAttribute("error", "全てのフィールドに入力してください。");
            return "login/register";
        }

        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "パスワードが一致しません。");
            return "login/register";
        }

        // パスワードのハッシュ化処理
        String hashedPassword = new BCryptPasswordEncoder().encode(password);

        // ユーザー登録用エンティティの作成
        MasterUser user = new MasterUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        // 登録処理（データベースへの保存）
        userMapper.insert(user);

        model.addAttribute("message", "アカウント登録が完了しました！");
        model.addAttribute("username", username);
        return "login/login";
    }
}
