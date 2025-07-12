CREATE TABLE tb_user (
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP,
    status VARCHAR(50) NOT NULL,
    user_role VARCHAR(20)
);

CREATE TABLE tb_product (
    id UUID PRIMARY KEY,
    batch_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(15, 2) NOT NULL,
    validity_start DATE,
    validity_end DATE,
    created_on TIMESTAMP,
    updated_on TIMESTAMP
);

CREATE TABLE tb_order (
    order_id UUID PRIMARY KEY,
    order_number BIGINT NOT NULL UNIQUE,
    user_id UUID,
    status VARCHAR(50),
    total_amount NUMERIC(10,2),
    observation TEXT,
    created_on TIMESTAMP,
    updated_on TIMESTAMP,
    CONSTRAINT fk_tb_order_user FOREIGN KEY (user_id) REFERENCES tb_user(id)
);

CREATE TABLE order_product (
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES tb_order(order_id),
    CONSTRAINT fk_product FOREIGN KEY (product_id) REFERENCES tb_product(id)
);