CREATE INDEX idx_orders_external_id ON orders(external_id);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_products_order_id ON products(order_id);

