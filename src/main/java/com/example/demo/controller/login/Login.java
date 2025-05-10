package com.example.demo.controller.login;

import com.example.demo.entity.MasterUser;
import com.example.demo.form.UserValidation;
import com.example.demo.mapper.UserMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.charset.StandardCharsets;

import static java.net.URLEncoder.encode;

@Controller
@RequiredArgsConstructor
public class Login {

    private final UserMapper userMapper;

    @GetMapping("/login")
    public String loginPage() {
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserValidation form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "login/login";
        }
        return "redirect:/list";
    }

    @GetMapping("/public")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String home(@AuthenticationPrincipal com.example.demo.security.UserDetails user, HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String url = "https://wiki.aristos.server-on.net/";
        if (user != null && user.getUsername().equals("admin") &&
                ("127.0.0.1".equals(remoteAddr) || "0:0:0:0:0:0:0:1".equals(remoteAddr))) {
            return "redirect:/register";
        }
        if (user != null && !ObjectUtils.isEmpty(user)) {
            url += "?user=" + encode(String.valueOf(user.getUsername()), StandardCharsets.UTF_8);
        }
        return "redirect:" + url;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "login/register";
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String registerUser(
            @AuthenticationPrincipal com.example.demo.security.UserDetails user1, @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {

        // 入力値のチェック
        if (user1.getUsername().equals("admin") || username == null || username.isEmpty() ||
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
