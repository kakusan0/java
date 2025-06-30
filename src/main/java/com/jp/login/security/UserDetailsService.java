package com.jp.login.security;

import com.jp.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMapper.existsByUsername(username)
                .map(com.jp.login.security.UserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("MasterUser not found: " + username));
    }
}