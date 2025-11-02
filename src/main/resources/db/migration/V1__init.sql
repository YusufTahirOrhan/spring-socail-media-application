CREATE TABLE users (
                       id            BIGSERIAL PRIMARY KEY,
                       username      VARCHAR(64) NOT NULL UNIQUE,
                       password_hash VARCHAR(100) NOT NULL,
                       role          VARCHAR(50) NOT NULL,
                       deleted       BOOLEAN NOT NULL DEFAULT FALSE,
                       created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE TABLE tokens (
                        id            BIGSERIAL PRIMARY KEY,
                        user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                        token_hash    VARCHAR(64) NOT NULL,
                        issued_at     TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                        expires_at    TIMESTAMP WITH TIME ZONE NOT NULL,
                        revoked_at    TIMESTAMP WITH TIME ZONE,
                        user_agent    VARCHAR(255),
                        ip_address    VARCHAR(50)
);
CREATE INDEX idx_tokens_token_hash ON tokens(token_hash);


CREATE TABLE posts (
                       id            BIGSERIAL PRIMARY KEY,
                       author_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       image_path    VARCHAR(255) NOT NULL,
                       description   TEXT,
                       like_count    INTEGER NOT NULL DEFAULT 0,
                       view_count    INTEGER NOT NULL DEFAULT 0,
                       created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                       updated_at    TIMESTAMP WITH TIME ZONE,
                       deleted       BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE comments (
                          id            BIGSERIAL PRIMARY KEY,
                          post_id       BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                          author_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                          content       TEXT NOT NULL,
                          created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                          deleted       BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE likes (
                       user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                       post_id       BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
                       created_at    TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                       PRIMARY KEY (user_id, post_id)
);