package com.challenge.ecommercebackend.modules.product.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductStatusTest {

    @Test
    @DisplayName("Debe tener los valores ACTIVE e INACTIVE")
    void shouldHaveExpectedValues() {
        assertEquals(2, ProductStatus.values().length);
        assertNotNull(ProductStatus.ACTIVE);
        assertNotNull(ProductStatus.INACTIVE);
    }

    @Test
    @DisplayName("Debe retornar displayName correcto para ACTIVE")
    void shouldReturnCorrectDisplayNameForActive() {
        assertEquals("Activo", ProductStatus.ACTIVE.getDisplayName());
    }

    @Test
    @DisplayName("Debe retornar displayName correcto para INACTIVE")
    void shouldReturnCorrectDisplayNameForInactive() {
        assertEquals("Inactivo", ProductStatus.INACTIVE.getDisplayName());
    }

    @Test
    @DisplayName("Debe convertir desde string con valueOf")
    void shouldConvertFromString() {
        assertEquals(ProductStatus.ACTIVE, ProductStatus.valueOf("ACTIVE"));
        assertEquals(ProductStatus.INACTIVE, ProductStatus.valueOf("INACTIVE"));
    }

    @Test
    @DisplayName("Debe lanzar excepción con valor inválido")
    void shouldThrowWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> ProductStatus.valueOf("UNKNOWN"));
    }
}

