package com.example.demo.security;

import com.example.demo.entity.post.MasterUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    @Getter
    private final MasterUser loginUser;
    private final Collection<? extends GrantedAuthority> authorities;

    // コンストラクタ: MasterUserを受け取り、権限を設定する
    public UserDetails(MasterUser loginUser) {
        this.loginUser = loginUser;
        this.authorities = loginUser.getRole() != null
                ? loginUser.getRole().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    // ユーザーに付与された権限を返す
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // ユーザーのパスワードを返す
    @Override
    public String getPassword() {
        return loginUser.getPassword();
    }

    // ユーザー名を返す
    @Override
    public String getUsername() {
        return loginUser.getUsername();
    }

    // アカウントが有効期限切れかどうかを判定する (今回は常に有効としている)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // アカウントがロックされていないかどうかを判定する
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 資格情報が有効期限切れかどうかを判定する
    @Override
    public boolean isCredentialsNonExpired() {
        return loginUser.isCredentialsNonExpired();
    }

    // ユーザーが有効であるかどうかを判定する
    @Override
    public boolean isEnabled() {
        return loginUser.isEnabled();
    }
}