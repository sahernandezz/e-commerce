package com.challenge.ecommercebackend.modules.product.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryStatusTest {

    @Test
    @DisplayName("Debe tener los valores ACTIVE e INACTIVE")
    void shouldHaveExpectedValues() {
        assertEquals(2, CategoryStatus.values().length);
        assertNotNull(CategoryStatus.ACTIVE);
        assertNotNull(CategoryStatus.INACTIVE);
    }

    @Test
    @DisplayName("Debe retornar displayName correcto")
    void shouldReturnCorrectDisplayNames() {
        assertEquals("Activo", CategoryStatus.ACTIVE.getDisplayName());
        assertEquals("Inactivo", CategoryStatus.INACTIVE.getDisplayName());
    }

    @Test
    @DisplayName("Debe convertir desde string con valueOf")
    void shouldConvertFromString() {
        assertEquals(CategoryStatus.ACTIVE, CategoryStatus.valueOf("ACTIVE"));
        assertEquals(CategoryStatus.INACTIVE, CategoryStatus.valueOf("INACTIVE"));
    }

    @Test
    @DisplayName("Debe lanzar excepción con valor inválido")
    void shouldThrowWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> CategoryStatus.valueOf("DELETED"));
    }
}

