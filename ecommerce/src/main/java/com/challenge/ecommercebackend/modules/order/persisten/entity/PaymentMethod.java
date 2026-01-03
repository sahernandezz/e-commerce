package com.challenge.ecommercebackend.modules.order.persisten.entity;

/**
 * Enum que representa el método de pago de una orden.
 * Mapea directamente con el tipo orders.payment_method de PostgreSQL.
 */
public enum PaymentMethod {
    CREDIT_CARD("Tarjeta de crédito"),
    DEBIT_CARD("Tarjeta de débito"),
    PAYPAL("PayPal"),
    CASH("Efectivo"),
    TRANSFER("Transferencia");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
