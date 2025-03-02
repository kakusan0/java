package com.example.demo.entity.post;

import lombok.Data;

import java.sql.Date;
import java.time.LocalDate;
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
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private Date credentialsNonExpired;

    public boolean getCredentialsNonExpired() {
        if (credentialsNonExpired == null) {
            return true;
        }
        return !credentialsNonExpired.before(Date.valueOf(LocalDate.now()));
    }
}