-- =============================================
-- Módulo ORDERS: Tablas de órdenes y pedidos
-- =============================================

-- Tabla de órdenes
CREATE TABLE orders.orders
(
    id             BIGSERIAL PRIMARY KEY,
    address        VARCHAR(255) NOT NULL,
    city           VARCHAR(255) NOT NULL,
    created_at     TIMESTAMP(6) DEFAULT CURRENT_TIMESTAMP,
    description    VARCHAR(255),
    email_customer VARCHAR(255) NOT NULL,
    order_code     VARCHAR(255) NOT NULL,
    payment_method orders.payment_method NOT NULL,
    status         orders.order_status NOT NULL DEFAULT 'PENDING',
    total          INTEGER      NOT NULL,
    updated_at     TIMESTAMP(6)
);

-- Tabla de productos en órdenes
CREATE TABLE orders.order_product
(
    id         BIGSERIAL PRIMARY KEY,
    color      VARCHAR(255),
    discount   INTEGER,
    id_product BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    quantity   INTEGER      NOT NULL,
    size       VARCHAR(255),
    total      INTEGER      NOT NULL,
    unit_price INTEGER      NOT NULL,
    id_order   BIGINT       NOT NULL,
    CONSTRAINT fk_order_product_order FOREIGN KEY (id_order) REFERENCES orders.orders (id) ON DELETE CASCADE
);

-- Tabla de descuentos por orden
CREATE TABLE orders.discount_order
(
    id_order BIGINT NOT NULL,
    discount INTEGER,
    CONSTRAINT fk_discount_order FOREIGN KEY (id_order) REFERENCES orders.orders (id) ON DELETE CASCADE
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_orders_email ON orders.orders (email_customer);
CREATE INDEX idx_orders_status ON orders.orders (status);
CREATE INDEX idx_orders_code ON orders.orders (order_code);
CREATE INDEX idx_order_product_order ON orders.order_product (id_order);

