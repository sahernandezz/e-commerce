package com.challenge.ecommercebackend.modules.order.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderStatusTest {

    @Test
    @DisplayName("Debe tener 6 estados de orden")
    void shouldHaveSixValues() {
        assertEquals(6, OrderStatus.values().length);
    }

    @Test
    @DisplayName("Debe retornar displayName correcto para todos los estados")
    void shouldReturnCorrectDisplayNames() {
        assertEquals("Pendiente", OrderStatus.PENDING.getDisplayName());
        assertEquals("Confirmado", OrderStatus.CONFIRMED.getDisplayName());
        assertEquals("En proceso", OrderStatus.PROCESSING.getDisplayName());
        assertEquals("Enviado", OrderStatus.SHIPPED.getDisplayName());
        assertEquals("Entregado", OrderStatus.DELIVERED.getDisplayName());
        assertEquals("Cancelado", OrderStatus.CANCELED.getDisplayName());
    }

    @Test
    @DisplayName("Debe convertir desde string con valueOf")
    void shouldConvertFromString() {
        assertEquals(OrderStatus.PENDING, OrderStatus.valueOf("PENDING"));
        assertEquals(OrderStatus.CANCELED, OrderStatus.valueOf("CANCELED"));
    }

    @Test
    @DisplayName("Debe lanzar excepción con valor inválido")
    void shouldThrowWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> OrderStatus.valueOf("REFUNDED"));
    }
}

