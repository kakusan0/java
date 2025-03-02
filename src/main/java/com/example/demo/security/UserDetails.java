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
        return loginUser.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return loginUser.getCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return loginUser.isEnabled();
    }
}