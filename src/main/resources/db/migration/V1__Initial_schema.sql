-- Initial schema creation
CREATE TABLE mst_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    password_expiry_date DATE DEFAULT NULL,
    credentials_non_expired DATE DEFAULT NULL,
    account_non_expired DATE DEFAULT NULL,
    discord_status BOOLEAN DEFAULT FALSE,
    enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(255) NOT NULL,
    account_non_locked BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (username)
);

CREATE TABLE content_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE webauthn_credentials (
    credential_id VARBINARY(255) PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    public_key_cose BLOB NOT NULL,
    user_handle VARBINARY(255) NOT NULL,
    sign_count BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (username) REFERENCES mst_user(username) ON DELETE CASCADE
);

-- Insert default data
INSERT INTO mst_user (
    id,
    username,
    password,
    password_expiry_date,
    credentials_non_expired,
    account_non_expired,
    discord_status,
    enabled,
    role,
    account_non_locked,
    created_at,
    updated_at
)
VALUES (
    1,
    'admin',
    '$2a$10$j/tXs0s2jP3FNeIxsnl/qe6Ejn16xvgdgcE0JZ2Hhe3Nv18/ryYCe',
    NULL,
    NULL,
    NULL,
    FALSE,
    TRUE,
    'ROLE_ADMIN',
    TRUE,
    '2025-05-27 15:55:06',
    '2025-05-27 15:55:06'
);

INSERT INTO content_items (item_name)
VALUES ('ホーム'),
    ('ダッシュボード'),
    ('設定');