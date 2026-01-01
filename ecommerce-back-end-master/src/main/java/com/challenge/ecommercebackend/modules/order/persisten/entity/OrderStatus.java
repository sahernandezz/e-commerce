package com.challenge.ecommercebackend.modules.order.persisten.entity;

/**
 * Enum que representa el estado de una orden.
 * Mapea directamente con el tipo orders.order_status de PostgreSQL.
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    DELIVERED
}
