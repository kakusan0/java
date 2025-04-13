CREATE TABLE users
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,                              -- ユーザーの一意な識別子
    username                VARCHAR(255) NOT NULL UNIQUE,                                   -- ユーザー名（ユニーク制約付き）
    password                VARCHAR(255) NOT NULL,                                          -- ユーザーのパスワード
    email                   VARCHAR(255) UNIQUE,                                            -- ユーザーの電子メールアドレス（ユニーク制約付き）
    discord_name            VARCHAR(255),                                                   -- ユーザーのDiscord名
    password_expiry_date    DATE,                                                           -- パスワードの有効期限日
    credentials_non_expired DATE,                                                           -- 資格情報の有効期限日
    account_non_expired     DATE,                                                           -- アカウントの有効期限日
    discord_status          BOOLEAN   DEFAULT FALSE,                                        -- Discord認証のステータス
    enabled                 BOOLEAN   DEFAULT TRUE,                                         -- アカウントが有効かどうか
    account_non_locked      BOOLEAN   DEFAULT TRUE,                                         -- アカウントがロックされていないかどうか
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                            -- 作成日時
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 更新日時
);