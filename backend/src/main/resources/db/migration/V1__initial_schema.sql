CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP,
    status VARCHAR(50) NOT NULL
);