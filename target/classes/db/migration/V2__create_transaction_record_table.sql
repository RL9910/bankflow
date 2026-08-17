CREATE TABLE transaction_record (
    id BIGSERIAL PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    amount NUMERIC(19, 2) NOT NULL,
    from_account_id BIGINT,
    to_account_id BIGINT,
    created_at TIMESTAMP NOT NULL
);