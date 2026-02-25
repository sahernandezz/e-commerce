-- =============================================
-- Vistas para CQRS (lectura optimizada con soft delete)
-- Estas vistas solo muestran registros activos (no eliminados)
-- =============================================

-- =============================================
-- VISTAS - AUTH
-- =============================================

-- Vista de usuarios activos con su rol
CREATE OR REPLACE VIEW auth.customer_view AS
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
WHERE c.status = 'ACTIVE'::auth.user_status AND r.status = 'ACTIVE'::auth.role_status;

-- Vista de roles activos
CREATE OR REPLACE VIEW auth.role_view AS
SELECT
    role_id,
    name,
    status
FROM auth.role
WHERE status = 'ACTIVE'::auth.role_status;

-- =============================================
-- VISTAS - CATALOG
-- =============================================

-- Vista de categorías activas
CREATE OR REPLACE VIEW catalog.category_view AS
SELECT
    id,
    name,
    status
FROM catalog.category
WHERE status = 'ACTIVE'::catalog.category_status;

-- Vista de productos activos
CREATE OR REPLACE VIEW catalog.product_view AS
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
WHERE p.status = 'ACTIVE'::catalog.product_status AND c.status = 'ACTIVE'::catalog.category_status;

-- Vista de colores de productos activos
CREATE OR REPLACE VIEW catalog.product_colors_view AS
SELECT
    pc.product_id,
    pc.colors
FROM catalog.product_colors pc
INNER JOIN catalog.product p ON p.id = pc.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status;

-- Vista de imágenes de productos activos
CREATE OR REPLACE VIEW catalog.product_images_view AS
SELECT
    pi.product_id,
    pi.images_url
FROM catalog.product_images_url pi
INNER JOIN catalog.product p ON p.id = pi.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status;

-- Vista de tallas de productos activos
CREATE OR REPLACE VIEW catalog.product_sizes_view AS
SELECT
    ps.product_id,
    ps.sizes
FROM catalog.product_sizes ps
INNER JOIN catalog.product p ON p.id = ps.product_id
WHERE p.status = 'ACTIVE'::catalog.product_status;

-- =============================================
-- VISTAS - ORDERS
-- =============================================

-- Vista de órdenes activas (no canceladas)
CREATE OR REPLACE VIEW orders.orders_view AS
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
WHERE status != 'CANCELED'::orders.order_status;

-- Vista de productos en órdenes activas
CREATE OR REPLACE VIEW orders.order_product_view AS
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
WHERE o.status != 'CANCELED'::orders.order_status;

-- =============================================
-- Permisos para las vistas
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

-- Usuario de escritura también tiene acceso a las vistas
GRANT SELECT ON auth.customer_view TO ecommerce_writer;
GRANT SELECT ON auth.role_view TO ecommerce_writer;
GRANT SELECT ON catalog.category_view TO ecommerce_writer;
GRANT SELECT ON catalog.product_view TO ecommerce_writer;
GRANT SELECT ON catalog.product_colors_view TO ecommerce_writer;
GRANT SELECT ON catalog.product_images_view TO ecommerce_writer;
GRANT SELECT ON catalog.product_sizes_view TO ecommerce_writer;
GRANT SELECT ON orders.orders_view TO ecommerce_writer;
GRANT SELECT ON orders.order_product_view TO ecommerce_writer;

