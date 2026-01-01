-- =============================================
-- Creación de usuarios para CQRS (lectura/escritura)
-- =============================================

-- Usuario para escritura (todas las operaciones DML)
CREATE USER ecommerce_writer WITH PASSWORD 'writer_secure_password_123';

-- Usuario para lectura (solo SELECT)
CREATE USER ecommerce_reader WITH PASSWORD 'reader_secure_password_123';

-- =============================================
-- Creación de esquemas por módulos
-- =============================================

-- Esquema para el módulo de usuarios y autenticación
CREATE SCHEMA IF NOT EXISTS auth;

-- Esquema para el módulo de productos y categorías
CREATE SCHEMA IF NOT EXISTS catalog;

-- Esquema para el módulo de órdenes
CREATE SCHEMA IF NOT EXISTS orders;

-- =============================================
-- Creación de TIPOS ENUM para estados
-- =============================================

-- ENUMs para el módulo AUTH
CREATE TYPE auth.user_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE auth.role_status AS ENUM ('ACTIVE', 'INACTIVE');

-- ENUMs para el módulo CATALOG
CREATE TYPE catalog.category_status AS ENUM ('ACTIVE', 'INACTIVE');
CREATE TYPE catalog.product_status AS ENUM ('ACTIVE', 'INACTIVE');

-- ENUMs para el módulo ORDERS
CREATE TYPE orders.order_status AS ENUM ('PENDING', 'CONFIRMED', 'CANCELED', 'DELIVERED');
CREATE TYPE orders.payment_method AS ENUM ('CREDIT_CARD', 'DEBIT_CARD', 'PAYPAL', 'CASH');

-- =============================================
-- Permisos para usuario de escritura
-- =============================================
GRANT USAGE ON SCHEMA auth TO ecommerce_writer;
GRANT USAGE ON SCHEMA catalog TO ecommerce_writer;
GRANT USAGE ON SCHEMA orders TO ecommerce_writer;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA auth TO ecommerce_writer;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA catalog TO ecommerce_writer;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA orders TO ecommerce_writer;

GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA auth TO ecommerce_writer;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA catalog TO ecommerce_writer;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA orders TO ecommerce_writer;

-- Permisos para tipos ENUM
GRANT USAGE ON TYPE auth.user_status TO ecommerce_writer;
GRANT USAGE ON TYPE auth.role_status TO ecommerce_writer;
GRANT USAGE ON TYPE catalog.category_status TO ecommerce_writer;
GRANT USAGE ON TYPE catalog.product_status TO ecommerce_writer;
GRANT USAGE ON TYPE orders.order_status TO ecommerce_writer;
GRANT USAGE ON TYPE orders.payment_method TO ecommerce_writer;

-- Permisos para futuras tablas
ALTER DEFAULT PRIVILEGES IN SCHEMA auth GRANT ALL PRIVILEGES ON TABLES TO ecommerce_writer;
ALTER DEFAULT PRIVILEGES IN SCHEMA catalog GRANT ALL PRIVILEGES ON TABLES TO ecommerce_writer;
ALTER DEFAULT PRIVILEGES IN SCHEMA orders GRANT ALL PRIVILEGES ON TABLES TO ecommerce_writer;

ALTER DEFAULT PRIVILEGES IN SCHEMA auth GRANT ALL PRIVILEGES ON SEQUENCES TO ecommerce_writer;
ALTER DEFAULT PRIVILEGES IN SCHEMA catalog GRANT ALL PRIVILEGES ON SEQUENCES TO ecommerce_writer;
ALTER DEFAULT PRIVILEGES IN SCHEMA orders GRANT ALL PRIVILEGES ON SEQUENCES TO ecommerce_writer;

-- =============================================
-- Permisos para usuario de lectura
-- =============================================
GRANT USAGE ON SCHEMA auth TO ecommerce_reader;
GRANT USAGE ON SCHEMA catalog TO ecommerce_reader;
GRANT USAGE ON SCHEMA orders TO ecommerce_reader;

GRANT SELECT ON ALL TABLES IN SCHEMA auth TO ecommerce_reader;
GRANT SELECT ON ALL TABLES IN SCHEMA catalog TO ecommerce_reader;
GRANT SELECT ON ALL TABLES IN SCHEMA orders TO ecommerce_reader;

-- Permisos para tipos ENUM (lectura)
GRANT USAGE ON TYPE auth.user_status TO ecommerce_reader;
GRANT USAGE ON TYPE auth.role_status TO ecommerce_reader;
GRANT USAGE ON TYPE catalog.category_status TO ecommerce_reader;
GRANT USAGE ON TYPE catalog.product_status TO ecommerce_reader;
GRANT USAGE ON TYPE orders.order_status TO ecommerce_reader;
GRANT USAGE ON TYPE orders.payment_method TO ecommerce_reader;

-- Permisos para futuras tablas (solo lectura)
ALTER DEFAULT PRIVILEGES IN SCHEMA auth GRANT SELECT ON TABLES TO ecommerce_reader;
ALTER DEFAULT PRIVILEGES IN SCHEMA catalog GRANT SELECT ON TABLES TO ecommerce_reader;
ALTER DEFAULT PRIVILEGES IN SCHEMA orders GRANT SELECT ON TABLES TO ecommerce_reader;

-- Mantener permisos para postgres (admin)
GRANT ALL ON SCHEMA auth TO postgres;
GRANT ALL ON SCHEMA catalog TO postgres;
GRANT ALL ON SCHEMA orders TO postgres;

