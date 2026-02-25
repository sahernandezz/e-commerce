-- =============================================
-- Datos iniciales - Módulo ORDERS
-- =============================================

-- Insertar órdenes de prueba
INSERT INTO orders.orders (id, address, city, created_at, description, email_customer, order_code, payment_method, status, total, updated_at)
VALUES
(1, 'Calle 123 #45-67', 'Bogotá', NOW() - INTERVAL '5 days', 'Entrega en horario de oficina', 'admin@ecommerce.com', 'ORD-001-2026', 'CREDIT_CARD', 'DELIVERED', 190000, NOW() - INTERVAL '1 day'),
(2, 'Carrera 50 #30-20', 'Medellín', NOW() - INTERVAL '3 days', NULL, 'admin@ecommerce.com', 'ORD-002-2026', 'PAYPAL', 'CONFIRMED', 70000, NOW() - INTERVAL '2 days'),
(3, 'Avenida 10 #15-30', 'Cali', NOW() - INTERVAL '1 day', 'Llamar antes de entregar', 'cliente1@test.com', 'ORD-003-2026', 'DEBIT_CARD', 'PENDING', 120000, NULL),
(4, 'Calle 80 #10-50', 'Barranquilla', NOW() - INTERVAL '7 days', NULL, 'cliente2@test.com', 'ORD-004-2026', 'CREDIT_CARD', 'CANCELED', 85000, NOW() - INTERVAL '6 days'),
(5, 'Carrera 15 #100-25', 'Bogotá', NOW() - INTERVAL '2 days', 'Dejar en portería', 'cliente3@test.com', 'ORD-005-2026', 'CASH', 'CONFIRMED', 245000, NOW());

-- Insertar productos de las órdenes
-- Orden 1 (DELIVERED)
INSERT INTO orders.order_product (id, color, discount, id_product, name, quantity, size, total, unit_price, id_order)
VALUES
(1, NULL, NULL, 1, 'Acme Slip-On Shoes', 1, '9', 120000, 120000, 1),
(2, 'Black', NULL, 2, 'Acme Drawstring Bag', 1, '7 x 9 inch', 70000, 70000, 1);

-- Orden 2 (CONFIRMED)
INSERT INTO orders.order_product (id, color, discount, id_product, name, quantity, size, total, unit_price, id_order)
VALUES
(3, 'White', NULL, 2, 'Acme Drawstring Bag', 1, '6 x 8 inch', 70000, 70000, 2);

-- Orden 3 (PENDING)
INSERT INTO orders.order_product (id, color, discount, id_product, name, quantity, size, total, unit_price, id_order)
VALUES
(4, NULL, NULL, 1, 'Acme Slip-On Shoes', 1, '10', 120000, 120000, 3);

-- Orden 4 (CANCELED)
INSERT INTO orders.order_product (id, color, discount, id_product, name, quantity, size, total, unit_price, id_order)
VALUES
(5, 'Black', 5000, 2, 'Acme Drawstring Bag', 1, '8 x 11 inch', 65000, 70000, 4),
(6, 'Black', NULL, 5, 'Acme Cup', 1, NULL, 20000, 20000, 4);

-- Orden 5 (CONFIRMED)
INSERT INTO orders.order_product (id, color, discount, id_product, name, quantity, size, total, unit_price, id_order)
VALUES
(7, NULL, NULL, 1, 'Acme Slip-On Shoes', 2, '8', 240000, 120000, 5);

-- Actualizar secuencias
SELECT setval('orders.orders_id_seq', (SELECT MAX(id) FROM orders.orders));
SELECT setval('orders.order_product_id_seq', (SELECT MAX(id) FROM orders.order_product));

