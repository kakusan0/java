package com.example.demo.security;

import com.example.demo.mapper.post.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultOAuth2UserService extends org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService {

    private final UserMapper userMapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // username を取得
        String username = oAuth2User.getAttribute("username");
        int userCount = userMapper.existsByUsernameDiscord(username);

        if (userCount == 0) {
            // カスタム例外をスローする
            throw new OAuth2AuthenticationException("User does not exist. Redirecting to home.");
        }

        // ユーザーが存在する場合のみ登録などを実行
//        userMapper.insertDiscord(username);

        return oAuth2User;
    }
}