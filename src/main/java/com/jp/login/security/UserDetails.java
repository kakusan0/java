package com.jp.login.security;

import com.jp.login.entity.user;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    @Getter
    private final user loginUser;
    private final Collection<? extends GrantedAuthority> authorities;

    // コンストラクタ: MasterUserを受け取り、権限を設定する
    public UserDetails(user loginUser) {
        this.loginUser = loginUser;
        this.authorities = List.of(new SimpleGrantedAuthority(loginUser.getRole()));
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