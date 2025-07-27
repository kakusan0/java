package com.jp.login.entity;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data
public class MasterUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private Long id; // ユーザーの一意な識別子
    private String username; // ユーザー名
    private String password; // ユーザーのパスワード
    private String role; // ユーザーに割り当てられたロールのリスト
    private String discordName; // ユーザーのDiscord名
    private Date passwordExpiryDate; // パスワードの有効期限日
    private Date credentialsNonExpired; // 資格情報の有効期限日
    private Date accountNonExpired; // アカウントの有効期限日
    private boolean discordStatus; // Discord認証のステータス
    private boolean enabled; // アカウントが有効かどうか
    private boolean accountNonLocked; // アカウントがロックされていないかどうか

    /**
     * パスワードの有効期限が切れているか判定します。
     *
     * ### 判断基準
     * - `passwordExpiryDate` が `null` の場合は、有効期限が無期限とみなされ `false` を返します。
     * - `passwordExpiryDate` が今日より前の日付の場合、期限切れとして `true` を返します。
     *
     * @return パスワードが期限切れであれば `true`
     */
    public boolean isPasswordExpired() {
        if (passwordExpiryDate == null) {
            return false; // 有効期限なし
        }
        return passwordExpiryDate.before(Date.valueOf(LocalDate.now()));
    }

    /**
     * 資格情報がまだ有効な場合は `true` を返します。
     * `credentialsNonExpired` が `null` の場合は有効と判断します。
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
