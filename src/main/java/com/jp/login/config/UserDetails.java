package com.jp.login.config;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.jp.login.entity.MstUser;

import lombok.Getter;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    @Getter
    private final MstUser loginUser;
    private final Collection<? extends GrantedAuthority> authorities;

    // コンストラクタ: MasterUserを受け取り、権限を設定する
    public UserDetails(MstUser user) {
        this.loginUser = user;
        // Normalize role strings into ordered set to preserve order and deduplicate
        java.util.LinkedHashSet<String> set = new java.util.LinkedHashSet<>();
        String roleField = user.getRole();
        if (roleField != null && !roleField.isBlank()) {
            for (String r : roleField.split(",")) {
                String t = r.trim();
                if (!t.isEmpty())
                    set.add(t);
            }
        }
        // always ensure ROLE_UserCheckOK exists (added at end)
        set.remove("ROLE_UserCheckOK");
        set.add("ROLE_UserCheckOK");
        this.authorities = java.util.Collections.unmodifiableList(
                set.stream().map(SimpleGrantedAuthority::new).toList());
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
        try {
            java.time.LocalDate d = loginUser.getCredentialsNonExpired();
            if (d == null)
                return true;
            return !d.isBefore(java.time.LocalDate.now());
        } catch (NoSuchMethodError | NullPointerException e) {
            return true;
        }
    }

    // ユーザーが有効であるかどうかを判定する
    @Override
    public boolean isEnabled() {
        Boolean b = loginUser.getEnabled();
        return Boolean.TRUE.equals(b);
    }
}
