CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    -- 他の必要なカラムを追加 (例: email, role, created_at, updated_at)
    email VARCHAR(255) UNIQUE,
    role VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);