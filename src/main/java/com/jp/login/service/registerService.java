package com.jp.login.service;

import com.jp.login.entity.MasterUser;
import com.jp.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class registerService {

    private final UserMapper userMapper;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int PASSWORD_LENGTH = 15;

    // パスワード自動生成メソッド
    public String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    @Transactional
    public String insert(String username) {
        // パスワードのハッシュ化処理
        String password = generateRandomPassword();
        String hashedPassword = new BCryptPasswordEncoder().encode(password);

        // ユーザー登録用エンティティの作成
        MasterUser user = new MasterUser();
        user.getUser().setUsername(username);
        user.getUser().setPassword(hashedPassword);

        // 登録処理（データベースへの保存）
        userMapper.insert(user);

        return password;
    }
}
