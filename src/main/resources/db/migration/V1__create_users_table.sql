CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    display_name  VARCHAR(100),
    role          VARCHAR(20)  NOT NULL DEFAULT 'USER',
    is_enabled    BOOLEAN      NOT NULL DEFAULT FALSE,
    is_locked     BOOLEAN      NOT NULL DEFAULT FALSE,
    failed_attempts INT        NOT NULL DEFAULT 0,
    lock_time     TIMESTAMP,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);