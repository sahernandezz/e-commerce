package com.challenge.ecommercebackend.modules.order.persisten.entity;

/**
 * Enum que representa el estado de una orden.
 * Mapea directamente con el tipo orders.order_status de PostgreSQL.
 */
public enum OrderStatus {
    PENDING("Pendiente"),
    PROCESSING("En proceso"),
    SHIPPED("Enviado"),
    DELIVERED("Entregado"),
    CANCELED("Cancelado");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
