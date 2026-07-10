CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    account_number VARCHAR(255) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_accounts_customer_id ON accounts(customer_id);
