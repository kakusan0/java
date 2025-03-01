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

    public UserDetails(MasterUser loginUser) {
        this.loginUser = loginUser;

        // MasterUser から権限情報を動的に設定する（必要ならカスタマイズ）
        // 例: 必要に応じて role フィールドまたは固定値 "ROLE_USER" を使用
        this.authorities = loginUser.getRoles() != null
                ? loginUser.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return loginUser.getPassword();
    }

    @Override
    public String getUsername() {
        return loginUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // アカウントは期限切れではないと仮定
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // アカウントはロックされていないと仮定
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 資格情報は期限切れではないと仮定
    }

    @Override
    public boolean isEnabled() {
        return loginUser.isEnabled(); // MasterUser の有効状態を使用
    }
}