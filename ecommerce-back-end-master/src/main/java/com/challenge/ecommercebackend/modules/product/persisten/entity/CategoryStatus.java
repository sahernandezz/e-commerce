package com.challenge.ecommercebackend.modules.product.persisten.entity;

/**
 * Enum que representa el estado de una categoría.
 * Mapea directamente con el tipo catalog.category_status de PostgreSQL.
 */
public enum CategoryStatus {
    ACTIVE,
    INACTIVE
}
