package com.example.login.service;

import com.example.login.entity.MasterUser;
import com.example.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class registerService {

    private final UserMapper userMapper;

    @Transactional
    public void add(String user1, String username, String password, String confirmPassword) {

        // パスワードのハッシュ化処理
        String hashedPassword = new BCryptPasswordEncoder().encode(password);

        // ユーザー登録用エンティティの作成
        MasterUser user = new MasterUser();
        user.setUsername(username);
        user.setPassword(hashedPassword);

        // 登録処理（データベースへの保存）
        userMapper.insert(user);
    }
}
