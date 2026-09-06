CREATE TABLE contracts (
    id            UUID PRIMARY KEY,
    contract_name VARCHAR(255) NOT NULL UNIQUE,
    created_date  TIMESTAMP NOT NULL,
    balance       NUMERIC(38, 2) NOT NULL
);

CREATE TABLE transactions (
    id                 UUID PRIMARY KEY,
    source_contract_id UUID NOT NULL REFERENCES contracts(id),
    target_contract_id UUID NOT NULL REFERENCES contracts(id),
    source_user_id     UUID NOT NULL,
    target_user_id     UUID NOT NULL,
    amount             NUMERIC(38, 2) NOT NULL,
    description        TEXT,
    created_at         TIMESTAMP NOT NULL
);