INSERT INTO
    mst_user ( id, username, password, password_expiry_date, credentials_non_expired, account_non_expired
             , discord_status, enabled, role, account_non_locked, created_at, updated_at)
VALUES
    ( 1, 'admin', '$2a$10$j/tXs0s2jP3FNeIxsnl/qe6Ejn16xvgdgcE0JZ2Hhe3Nv18/ryYCe', NULL, NULL, NULL, FALSE, TRUE
    , 'ROLE_ADMIN', TRUE, '2025-05-27 15:55:06', '2025-05-27 15:55:06');

INSERT INTO
    content_items (item_name)
VALUES
    ('ホーム')
    , ('ダッシュボード')
    , ('設定');
