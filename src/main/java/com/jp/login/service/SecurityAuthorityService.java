package com.jp.login.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityAuthorityService {

    /**
     * Add a single authority/role to the current authentication, if present.
     */
    public void addAuthority(String authority) {
        if (authority == null || authority.isBlank()) {
            return;
        }
        addAuthorities(List.of(authority));
    }

    /**
     * Add multiple authorities/roles to the current authentication, if present.
     */
    public void addAuthorities(Collection<String> authorities) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }

        List<SimpleGrantedAuthority> updated = new ArrayList<>();
        authentication.getAuthorities().forEach(a -> updated.add(new SimpleGrantedAuthority(a.getAuthority())));

        if (authorities != null) {
            authorities.stream().filter(Objects::nonNull).map(String::trim).filter(s -> !s.isEmpty())
                    .forEach(r -> updated.add(new SimpleGrantedAuthority(r)));
        }

        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),
                authentication.getCredentials(), updated);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
