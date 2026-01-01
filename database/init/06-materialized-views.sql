-- =============================================
-- Vistas materializadas para CQRS (lectura optimizada con soft delete)
-- Estas vistas solo muestran registros activos (no eliminados)
-- =============================================

-- =============================================
-- VISTAS MATERIALIZADAS - AUTH
-- =============================================

-- Vista materializada de usuarios activos con su rol
CREATE MATERIALIZED VIEW auth.customer_view AS
SELECT
    c.id,
    c.email,
    c.name,
    c.password,
    c.recovery_code,
    c.recovery_date,
    c.status,
    c.role_id
FROM auth.customer c
INNER JOIN auth.role r ON r.role_id = c.role_id
WHERE c.status = 'ACTIVE'::auth.user_status AND r.status = 'ACTIVE'::auth.role_status
WITH DATA;

-- Índices para la vista materializada
CREATE UNIQUE INDEX idx_customer_view_id ON auth.customer_view (id);
CREATE INDEX idx_customer_view_email ON auth.customer_view (email);
CREATE INDEX idx_customer_view_role ON auth.customer_view (role_id);

-- Vista materializada de roles activos
CREATE MATERIALIZED VIEW auth.role_view AS
SELECT
    role_id,
    name,
    status
FROM auth.role
WHERE status = 'ACTIVE'::auth.role_status
WITH DATA;

CREATE UNIQUE INDEX idx_role_view_id ON auth.role_view (role_id);

-- =============================================
-- VISTAS MATERIALIZADAS - CATALOG
-- =============================================

-- Vista materializada de categorías activas
CREATE MATERIALIZED VIEW catalog.category_view AS
SELECT
    id,
    name,
    status
FROM catalog.category
WHERE status = 'ACTIVE'::catalog.category_status
WITH DATA;

CREATE UNIQUE INDEX idx_category_view_id ON catalog.category_view (id);
CREATE INDEX idx_category_view_name ON catalog.category_view (name);

-- Vista materializada de productos activos
CREATE MATERIALIZED VIEW catalog.product_view AS
SELECT
    p.id,
    p.created_at,
    p.description,
    p.discount,
    p.name,
    p.price,
    p.status,
    p.updated_at,
    p.category_id,
    c.name as category_name
FROM catalog.product p
INNER JOIN catalog.category c ON c.id = p.category_id
WHERE p.status = 'ACTIVE'::catalog.product_status AND c.status = 'ACTIVE'::catalog.category_status
WITH DATA;

CREATE UNIQUE INDEX idx_product_view_id ON catalog.product_view (id);
CREATE INDEX idx_product_view_category ON catalog.product_view (category_id);
CREATE INDEX idx_product_view_name ON catalog.product_view (name);

-- Vista materializada de colores de productos activos
CREATE MATERIALIZED VIEW catalog.product_colors_view AS
SELECT
    pc.product_id,
    pc.colors
FROM catalog.product_colors pc
INNER JOIN catalog.product p ON p.id = pc.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status
WITH DATA;

CREATE INDEX idx_product_colors_view_product ON catalog.product_colors_view (product_id);

-- Vista materializada de imágenes de productos activos
CREATE MATERIALIZED VIEW catalog.product_images_view AS
SELECT
    pi.product_id,
    pi.images_url
FROM catalog.product_images_url pi
INNER JOIN catalog.product p ON p.id = pi.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status
WITH DATA;

CREATE INDEX idx_product_images_view_product ON catalog.product_images_view (product_id);

-- Vista materializada de tallas de productos activos
CREATE MATERIALIZED VIEW catalog.product_sizes_view AS
SELECT
    ps.product_id,
    ps.sizes
FROM catalog.product_sizes ps
INNER JOIN catalog.product p ON p.id = ps.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status
WITH DATA;

CREATE INDEX idx_product_sizes_view_product ON catalog.product_sizes_view (product_id);

-- =============================================
-- VISTAS MATERIALIZADAS - ORDERS
-- =============================================

-- Vista materializada de órdenes activas (no canceladas)
CREATE MATERIALIZED VIEW orders.orders_view AS
SELECT
    id,
    address,
    city,
    created_at,
    description,
    email_customer,
    order_code,
    payment_method,
    status,
    total,
    updated_at
FROM orders.orders
WHERE status != 'CANCELED'::orders.order_status
WITH DATA;

CREATE UNIQUE INDEX idx_orders_view_id ON orders.orders_view (id);
CREATE INDEX idx_orders_view_email ON orders.orders_view (email_customer);
CREATE INDEX idx_orders_view_status ON orders.orders_view (status);
CREATE INDEX idx_orders_view_code ON orders.orders_view (order_code);

-- Vista materializada de productos en órdenes activas
CREATE MATERIALIZED VIEW orders.order_product_view AS
SELECT
    op.id,
    op.color,
    op.discount,
    op.id_product,
    op.name,
    op.quantity,
    op.size,
    op.total,
    op.unit_price,
    op.id_order
FROM orders.order_product op
INNER JOIN orders.orders o ON o.id = op.id_order
WHERE o.status != 'CANCELED'::orders.order_status
WITH DATA;

CREATE UNIQUE INDEX idx_order_product_view_id ON orders.order_product_view (id);
CREATE INDEX idx_order_product_view_order ON orders.order_product_view (id_order);

-- =============================================
-- Permisos para las vistas materializadas
-- =============================================

-- Usuario de lectura puede acceder a todas las vistas
GRANT SELECT ON auth.customer_view TO ecommerce_reader;
GRANT SELECT ON auth.role_view TO ecommerce_reader;
GRANT SELECT ON catalog.category_view TO ecommerce_reader;
GRANT SELECT ON catalog.product_view TO ecommerce_reader;
GRANT SELECT ON catalog.product_colors_view TO ecommerce_reader;
GRANT SELECT ON catalog.product_images_view TO ecommerce_reader;
GRANT SELECT ON catalog.product_sizes_view TO ecommerce_reader;
GRANT SELECT ON orders.orders_view TO ecommerce_reader;
GRANT SELECT ON orders.order_product_view TO ecommerce_reader;

-- Usuario de escritura puede refrescar las vistas
GRANT ALL ON auth.customer_view TO ecommerce_writer;
GRANT ALL ON auth.role_view TO ecommerce_writer;
GRANT ALL ON catalog.category_view TO ecommerce_writer;
GRANT ALL ON catalog.product_view TO ecommerce_writer;
GRANT ALL ON catalog.product_colors_view TO ecommerce_writer;
GRANT ALL ON catalog.product_images_view TO ecommerce_writer;
GRANT ALL ON catalog.product_sizes_view TO ecommerce_writer;
GRANT ALL ON orders.orders_view TO ecommerce_writer;
GRANT ALL ON orders.order_product_view TO ecommerce_writer;

-- =============================================
-- Función para refrescar todas las vistas materializadas
-- =============================================

CREATE OR REPLACE FUNCTION refresh_all_materialized_views()
RETURNS void AS $$
BEGIN
    -- Auth views
    REFRESH MATERIALIZED VIEW CONCURRENTLY auth.customer_view;
    REFRESH MATERIALIZED VIEW CONCURRENTLY auth.role_view;

    -- Catalog views
    REFRESH MATERIALIZED VIEW CONCURRENTLY catalog.category_view;
    REFRESH MATERIALIZED VIEW CONCURRENTLY catalog.product_view;
    REFRESH MATERIALIZED VIEW catalog.product_colors_view;
    REFRESH MATERIALIZED VIEW catalog.product_images_view;
    REFRESH MATERIALIZED VIEW catalog.product_sizes_view;

    -- Orders views
    REFRESH MATERIALIZED VIEW CONCURRENTLY orders.orders_view;
    REFRESH MATERIALIZED VIEW orders.order_product_view;
END;
$$ LANGUAGE plpgsql;

-- Función para refrescar vistas por esquema
CREATE OR REPLACE FUNCTION refresh_auth_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY auth.customer_view;
    REFRESH MATERIALIZED VIEW CONCURRENTLY auth.role_view;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION refresh_catalog_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY catalog.category_view;
    REFRESH MATERIALIZED VIEW CONCURRENTLY catalog.product_view;
    REFRESH MATERIALIZED VIEW catalog.product_colors_view;
    REFRESH MATERIALIZED VIEW catalog.product_images_view;
    REFRESH MATERIALIZED VIEW catalog.product_sizes_view;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION refresh_orders_views()
RETURNS void AS $$
BEGIN
    REFRESH MATERIALIZED VIEW CONCURRENTLY orders.orders_view;
    REFRESH MATERIALIZED VIEW orders.order_product_view;
END;
$$ LANGUAGE plpgsql;

-- Permisos para ejecutar las funciones
GRANT EXECUTE ON FUNCTION refresh_all_materialized_views() TO ecommerce_writer;
GRANT EXECUTE ON FUNCTION refresh_auth_views() TO ecommerce_writer;
GRANT EXECUTE ON FUNCTION refresh_catalog_views() TO ecommerce_writer;
GRANT EXECUTE ON FUNCTION refresh_orders_views() TO ecommerce_writer;
