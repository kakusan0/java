package com.example.demo.security;

import com.example.demo.entity.post.MasterUser;
import com.example.demo.mapper.post.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load user by username: {}", username);
        int userCount = userMapper.existsByUsernameAndPassword(username);

        if (userCount > 0) {
            MasterUser user = userMapper.existsByUsernameAndPasswordAndId(username);
            return new AuthUserDetails(user);
        } else {
            log.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found: " + username);
        }
    }
}