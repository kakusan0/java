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
CREATE TABLE ZipCode
(
    id              INT AUTO_INCREMENT
        PRIMARY KEY,
    zip_code        VARCHAR(7)  NULL,
    prefecture_kana VARCHAR(7)  NULL,
    city_kana       VARCHAR(23) NULL,
    town_area_kana  VARCHAR(42) NULL,
    prefecture      VARCHAR(5)  NULL,
    city            VARCHAR(11) NULL,
    town_area       VARCHAR(37) NULL
) COLLATE = utf8mb4_unicode_ci;
-- auto-generated definition
create table SPRING_SESSION
(
    PRIMARY_ID            char(36)     not null
        primary key,
    SESSION_ID            char(36)     not null,
    CREATION_TIME         bigint       not null,
    LAST_ACCESS_TIME      bigint       not null,
    MAX_INACTIVE_INTERVAL int          not null,
    EXPIRY_TIME           bigint       not null,
    PRINCIPAL_NAME        varchar(100) null,
    constraint SPRING_SESSION_IX1
        unique (SESSION_ID)
);

create index SPRING_SESSION_IX2
    on SPRING_SESSION (EXPIRY_TIME);

create index SPRING_SESSION_IX3
    on SPRING_SESSION (PRINCIPAL_NAME);

-- auto-generated definition
create table SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID char(36)     not null,
    ATTRIBUTE_NAME     varchar(200) not null,
    ATTRIBUTE_BYTES    blob         not null,
    primary key (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    constraint SPRING_SESSION_ATTRIBUTES_FK
        foreign key (SESSION_PRIMARY_ID) references SPRING_SESSION (PRIMARY_ID)
            on delete cascade
);