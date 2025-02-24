package com.example.demo.security;

import com.example.demo.entity.post.MasterUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AuthUserDetails implements UserDetails {

    @Getter
    private final MasterUser loginUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUserDetails(MasterUser loginUser) {
        this.loginUser = loginUser;
        List<String> list = new ArrayList<>(Collections.singletonList("ROLE_USER"));
        this.authorities = list
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // 修正：実際のパスワードを返す
    @Override
    public String getPassword() {
        return loginUser.getPassword();
    }

    // 修正：実際のユーザー名を返す
    @Override
    public String getUsername() {
        return loginUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}