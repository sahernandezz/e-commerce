-- =============================================
-- Módulo CATALOG: Tablas de productos y categorías
-- =============================================

-- Tabla de categorías
CREATE TABLE catalog.category
(
    id     BIGSERIAL PRIMARY KEY,
    name   VARCHAR(50) NOT NULL UNIQUE,
    status catalog.category_status NOT NULL DEFAULT 'ACTIVE'
);

-- Tabla de productos
CREATE TABLE catalog.product
(
    id          BIGSERIAL PRIMARY KEY,
    created_at  TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(250),
    discount    INTEGER,
    name        VARCHAR(50) NOT NULL UNIQUE,
    price       INTEGER     NOT NULL,
    status      catalog.product_status NOT NULL DEFAULT 'ACTIVE',
    updated_at  TIMESTAMP(6),
    category_id BIGINT      NOT NULL,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES catalog.category (id)
);

-- Tabla de colores de producto
CREATE TABLE catalog.product_colors
(
    product_id BIGINT NOT NULL,
    colors     VARCHAR(255),
    CONSTRAINT fk_product_colors_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON DELETE CASCADE
);

-- Tabla de imágenes de producto
CREATE TABLE catalog.product_images_url
(
    product_id BIGINT NOT NULL,
    images_url VARCHAR(500),
    CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON DELETE CASCADE
);

-- Tabla de tallas de producto
CREATE TABLE catalog.product_sizes
(
    product_id BIGINT NOT NULL,
    sizes      VARCHAR(255),
    CONSTRAINT fk_product_sizes_product FOREIGN KEY (product_id) REFERENCES catalog.product (id) ON DELETE CASCADE
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_product_category ON catalog.product (category_id);
CREATE INDEX idx_product_status ON catalog.product (status);
CREATE INDEX idx_product_name ON catalog.product (name);
CREATE INDEX idx_category_status ON catalog.category (status);

