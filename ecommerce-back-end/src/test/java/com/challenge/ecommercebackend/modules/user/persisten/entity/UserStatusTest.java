package com.challenge.ecommercebackend.modules.user.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserStatusTest {

    @Test
    @DisplayName("Debe tener 3 estados de usuario")
    void shouldHaveThreeValues() {
        assertEquals(3, UserStatus.values().length);
    }

    @Test
    @DisplayName("Debe retornar displayName correcto")
    void shouldReturnCorrectDisplayNames() {
        assertEquals("Activo", UserStatus.ACTIVE.getDisplayName());
        assertEquals("Inactivo", UserStatus.INACTIVE.getDisplayName());
        assertEquals("Suspendido", UserStatus.SUSPENDED.getDisplayName());
    }

    @Test
    @DisplayName("Debe convertir desde string con valueOf")
    void shouldConvertFromString() {
        assertEquals(UserStatus.ACTIVE, UserStatus.valueOf("ACTIVE"));
        assertEquals(UserStatus.SUSPENDED, UserStatus.valueOf("SUSPENDED"));
    }

    @Test
    @DisplayName("Debe lanzar excepción con valor inválido")
    void shouldThrowWithInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> UserStatus.valueOf("BANNED"));
    }
}

