package com.challenge.ecommercebackend.modules.order.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMethodTest {

    @Test
    @DisplayName("Debe tener 5 métodos de pago")
    void shouldHaveFiveValues() {
        assertEquals(5, PaymentMethod.values().length);
    }

    @Test
    @DisplayName("Debe retornar displayName correcto para todos los métodos")
    void shouldReturnCorrectDisplayNames() {
        assertEquals("Tarjeta de crédito", PaymentMethod.CREDIT_CARD.getDisplayName());
        assertEquals("Tarjeta de débito", PaymentMethod.DEBIT_CARD.getDisplayName());
        assertEquals("PayPal", PaymentMethod.PAYPAL.getDisplayName());
        assertEquals("Efectivo", PaymentMethod.CASH.getDisplayName());
        assertEquals("Transferencia", PaymentMethod.TRANSFER.getDisplayName());
    }

    @Test
    @DisplayName("Debe convertir desde string con valueOf")
    void shouldConvertFromString() {
        assertEquals(PaymentMethod.CASH, PaymentMethod.valueOf("CASH"));
        assertEquals(PaymentMethod.CREDIT_CARD, PaymentMethod.valueOf("CREDIT_CARD"));
    }

    @Test
    @DisplayName("Debe lanzar excepción con valor inválido")
    void shouldThrowWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> PaymentMethod.valueOf("BITCOIN"));
    }
}

