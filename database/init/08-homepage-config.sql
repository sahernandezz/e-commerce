-- =============================================
-- Configuración de la portada (Homepage)
-- =============================================

-- Crear schema si no existe
CREATE SCHEMA IF NOT EXISTS config;

-- Tabla para la configuración de la portada
CREATE TABLE config.homepage (
    id BIGSERIAL PRIMARY KEY,
    -- Productos destacados (IDs de productos para mostrar en la portada)
    featured_product_main BIGINT REFERENCES catalog.product(id),
    featured_product_secondary1 BIGINT REFERENCES catalog.product(id),
    featured_product_secondary2 BIGINT REFERENCES catalog.product(id),
    -- Productos del carrusel (IDs separados por comas)
    carousel_product_ids TEXT,
    -- Banner principal
    banner_title VARCHAR(200),
    banner_subtitle VARCHAR(500),
    banner_image_url TEXT,
    banner_link VARCHAR(500),
    banner_enabled BOOLEAN DEFAULT false,
    -- Configuración general
    show_carousel BOOLEAN DEFAULT true,
    carousel_title VARCHAR(200) DEFAULT 'Productos Destacados',
    -- Metadatos
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(100)
);

-- Insertar configuración por defecto
INSERT INTO config.homepage (
    banner_title,
    banner_subtitle,
    banner_enabled,
    show_carousel,
    carousel_title
) VALUES (
    'Bienvenido a nuestra tienda',
    'Descubre los mejores productos',
    false,
    true,
    'Productos Destacados'
);

-- Permisos
GRANT SELECT ON config.homepage TO ecommerce_reader;
GRANT ALL ON config.homepage TO ecommerce_writer;
GRANT USAGE, SELECT ON SEQUENCE config.homepage_id_seq TO ecommerce_writer;

-- Función para actualizar el timestamp
CREATE OR REPLACE FUNCTION config.update_homepage_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger para actualizar timestamp automáticamente
CREATE TRIGGER homepage_update_timestamp
    BEFORE UPDATE ON config.homepage
    FOR EACH ROW
    EXECUTE FUNCTION config.update_homepage_timestamp();

