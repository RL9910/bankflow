CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    owner_name VARCHAR(255),
    balance NUMERIC(19, 2),
    user_id BIGINT NOT NULL,

    CONSTRAINT fk_account_user
        FOREIGN KEY (user_id)
        REFERENCES app_user(id)
);