package com.challenge.ecommercebackend.modules.order.persisten.entity;

/**
 * Enum que representa el método de pago de una orden.
 * Mapea directamente con el tipo orders.payment_method de PostgreSQL.
 */
public enum PaymentMethod {
    CREDIT_CARD,
    DEBIT_CARD,
    PAYPAL,
    CASH
}
