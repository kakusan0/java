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
    private String email;
    private List<String> roles;
    private String discordName;
    private Date passwordExpiryDate;
    private Date credentialsNonExpired;
    private Date accountNonExpired;
    private boolean discordStatus;
    private boolean enabled;
    private boolean accountNonLocked;

    /**
     * パスワードの有効期限が切れている場合は true を返します。
     * もし passwordExpiryDate が null の場合は、有効期限が設定されていないと判断し true を返します。
     */
    public boolean isPasswordExpired() {
        if (passwordExpiryDate == null) {
            return true;
        }
        // passwordExpiryDate が今日以降ならfalse（未期限切れ）、
        // それ以外なら true（期限切れ）
        return !passwordExpiryDate.after(Date.valueOf(LocalDate.now()));
    }

    /**
     * 資格情報がまだ有効な場合は true を返します。
     * credentialsNonExpired が null の場合は有効と判断します。
     */
    public boolean isCredentialsNonExpired() {
        if (credentialsNonExpired == null) {
            return true;
        }
        // credentialsNonExpired が今日以前なら false（期限切れ）、
        // 今日以降なら true（まだ有効）
        return !credentialsNonExpired.before(Date.valueOf(LocalDate.now()));
    }
}