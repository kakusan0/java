package com.example.login.security;

import com.example.login.entity.MasterUser;
import com.example.login.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        int userCount = userMapper.existsByUsername(username);

        if (userCount > 0) {
            MasterUser user = userMapper.existsByUsernameAndPasswordAndId(username);
            return new UserDetails(user);
        } else {
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}