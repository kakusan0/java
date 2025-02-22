//package com.example.demo.security;
//
//import lombok.AllArgsConstructor;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//
//@Service
//@AllArgsConstructor
//public class CustomUserDetailsService implements UserDetailsService {
//
//    private final ZipcodeMapper ZipcodeMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("ユーザが見つかりません: " + username));
//        // 権限は必要に応じて設定してください（ここでは簡易的に "USER" 権限を付与）
//        return new SecurityUser(user.getUsername(), user.getPassword(), Collections.singletonList(() -> "USER"));
//    }
//}