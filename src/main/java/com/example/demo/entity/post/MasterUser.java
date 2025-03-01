package com.example.demo.entity.post;

import lombok.Data;

import java.util.List;

@Data
public class MasterUser {
    private Long id;
    private String username;
    private String password;
    private String discordName;
    private String email;
    private List<String> roles;
    private boolean enabled;
}