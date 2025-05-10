CREATE TABLE mst_user
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,                              -- ユーザーの一意な識別子
    username                VARCHAR(255) NOT NULL UNIQUE,                                   -- ユーザー名（ユニーク制約付き）
    password                VARCHAR(255) NOT NULL,                                          -- ユーザーのパスワード（ハッシュ化推奨）
    email                   VARCHAR(255) UNIQUE,                                            -- ユーザーの電子メールアドレス（ユニーク制約付き、NULL可）
    discord_name            VARCHAR(255) UNIQUE,                                            -- ユーザーのDiscord名（ユニーク制約付き、NULL可）
    password_expiry_date    DATE,                                                           -- パスワードの有効期限日（NULLの場合は期限なし）
    credentials_non_expired DATE,                                                           -- 資格情報の有効期限日（NULLの場合は期限なし）
    account_non_expired     DATE,                                                           -- アカウントの有効期限日（NULLの場合は期限なし）
    discord_status          BOOLEAN   DEFAULT FALSE,                                        -- Discord認証のステータス（TRUE: 認証済み, FALSE: 未認証）
    enabled                 BOOLEAN   DEFAULT TRUE,                                         -- アカウントが有効かどうか（TRUE: 有効, FALSE: 無効）
    role                    VARCHAR(255) NOT NULL,                                          -- ユーザーの役割（例: ROLE_USER, ROLE_ADMIN など）
    account_non_locked      BOOLEAN   DEFAULT TRUE,                                         -- アカウントがロックされていないかどうか（TRUE: ロックなし, FALSE: ロック中）
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,                            -- レコード作成日時
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- レコード更新日時（自動更新）
);