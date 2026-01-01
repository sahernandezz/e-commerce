-- =============================================
-- Módulo AUTH: Tablas de usuarios y autenticación
-- =============================================

-- Tabla de roles
CREATE TABLE auth.role
(
    role_id SERIAL PRIMARY KEY,
    name    VARCHAR(25) NOT NULL UNIQUE,
    status  auth.role_status NOT NULL DEFAULT 'ACTIVE'
);

-- Tabla de usuarios (customers)
CREATE TABLE auth.customer
(
    id            BIGSERIAL PRIMARY KEY,
    email         VARCHAR(255) UNIQUE,
    name          VARCHAR(255),
    password      VARCHAR(255),
    recovery_code VARCHAR(255),
    recovery_date TIMESTAMP(6),
    status        auth.user_status NOT NULL DEFAULT 'ACTIVE',
    role_id       INTEGER NOT NULL,
    CONSTRAINT fk_customer_role FOREIGN KEY (role_id) REFERENCES auth.role (role_id)
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_customer_email ON auth.customer (email);
CREATE INDEX idx_customer_status ON auth.customer (status);
CREATE INDEX idx_customer_role ON auth.customer (role_id);
CREATE INDEX idx_role_status ON auth.role (status);

