CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    order_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    value NUMERIC(19, 2) NOT NULL,
    quantity INTEGER NOT NULL,
    CONSTRAINT fk_product_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

