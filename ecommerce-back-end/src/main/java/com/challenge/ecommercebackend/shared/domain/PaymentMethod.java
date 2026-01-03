package com.challenge.ecommercebackend.shared.domain;

/**
 * Métodos de pago estándar.
 * Compartido entre backend y frontend via GraphQL.
 */
public enum PaymentMethod {
    CREDIT_CARD("Tarjeta de crédito"),
    DEBIT_CARD("Tarjeta de débito"),
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

