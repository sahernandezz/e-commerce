package com.challenge.ecommercebackend.modules.product.persisten.entity;

/**
 * Enum que representa el estado de una categoría.
 * Mapea directamente con el tipo catalog.category_status de PostgreSQL.
 */
public enum CategoryStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo");

    private final String displayName;

    CategoryStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
