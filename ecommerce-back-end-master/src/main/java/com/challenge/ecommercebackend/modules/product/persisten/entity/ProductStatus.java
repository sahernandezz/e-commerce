package com.challenge.ecommercebackend.modules.product.persisten.entity;

/**
 * Enum que representa el estado de un producto.
 * Mapea directamente con el tipo catalog.product_status de PostgreSQL.
 */
public enum ProductStatus {
    ACTIVE,
    INACTIVE
}
