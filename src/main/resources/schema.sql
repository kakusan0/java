CREATE TABLE mst_user
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    username                VARCHAR(255) NOT NULL,
    password                VARCHAR(255) NOT NULL,
    password_expiry_date    DATE      DEFAULT NULL,
    credentials_non_expired DATE      DEFAULT NULL,
    account_non_expired     DATE      DEFAULT NULL,
    discord_status          BOOLEAN   DEFAULT FALSE,
    enabled                 BOOLEAN   DEFAULT TRUE,
    role                    VARCHAR(255) NOT NULL,
    account_non_locked      BOOLEAN   DEFAULT TRUE,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (username)
);

CREATE TABLE content_items
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_name  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
