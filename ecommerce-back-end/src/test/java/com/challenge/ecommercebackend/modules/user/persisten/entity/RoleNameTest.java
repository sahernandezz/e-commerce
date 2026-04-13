package com.challenge.ecommercebackend.modules.user.persisten.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleNameTest {

    @Test
    @DisplayName("Debe tener los valores ADMIN y USER")
    void shouldHaveExpectedValues() {
        assertEquals(2, RoleName.values().length);
        assertNotNull(RoleName.ADMIN);
        assertNotNull(RoleName.USER);
    }

    @Test
    @DisplayName("Debe retornar código correcto")
    void shouldReturnCorrectCode() {
        assertEquals("ADMIN", RoleName.ADMIN.getCode());
        assertEquals("USER", RoleName.USER.getCode());
    }

    @Test
    @DisplayName("Debe retornar displayName correcto")
    void shouldReturnCorrectDisplayName() {
        assertEquals("Administrador", RoleName.ADMIN.getDisplayName());
        assertEquals("Cliente", RoleName.USER.getDisplayName());
    }

    @Test
    @DisplayName("fromCode debe retornar el RoleName correcto")
    void shouldConvertFromCode() {
        assertEquals(RoleName.ADMIN, RoleName.fromCode("ADMIN"));
        assertEquals(RoleName.USER, RoleName.fromCode("USER"));
    }

    @Test
    @DisplayName("fromCode debe ser case-insensitive")
    void shouldBeCaseInsensitive() {
        assertEquals(RoleName.ADMIN, RoleName.fromCode("admin"));
        assertEquals(RoleName.USER, RoleName.fromCode("user"));
    }

    @Test
    @DisplayName("fromCode debe lanzar excepción con código inválido")
    void shouldThrowWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> RoleName.fromCode("SUPERADMIN"));
    }

    @Test
    @DisplayName("isValid debe retornar true para códigos válidos")
    void shouldReturnTrueForValidCodes() {
        assertTrue(RoleName.isValid("ADMIN"));
        assertTrue(RoleName.isValid("USER"));
    }

    @Test
    @DisplayName("isValid debe retornar false para códigos inválidos")
    void shouldReturnFalseForInvalidCodes() {
        assertFalse(RoleName.isValid("MODERATOR"));
        assertFalse(RoleName.isValid(""));
    }

    @Test
    @DisplayName("isAdmin debe funcionar correctamente")
    void shouldCheckIsAdmin() {
        assertTrue(RoleName.ADMIN.isAdmin());
        assertFalse(RoleName.USER.isAdmin());
    }

    @Test
    @DisplayName("isUser debe funcionar correctamente")
    void shouldCheckIsUser() {
        assertTrue(RoleName.USER.isUser());
        assertFalse(RoleName.ADMIN.isUser());
    }
}

