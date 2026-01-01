-- =============================================
-- Datos iniciales - Módulo AUTH
-- =============================================

-- Insertar roles por defecto
INSERT INTO auth.role (role_id, name, status) VALUES (1, 'USER', 'ACTIVE');
INSERT INTO auth.role (role_id, name, status) VALUES (2, 'ADMIN', 'ACTIVE');

-- Actualizar secuencia de roles
SELECT setval('auth.role_role_id_seq', (SELECT MAX(role_id) FROM auth.role));

-- Usuario admin de ejemplo (password: admin123)
-- La contraseña está encriptada con BCrypt
INSERT INTO auth.customer (email, name, password, status, role_id)
VALUES ('admin@ecommerce.com', 'Administrador', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', 'ACTIVE', 2);

