package com.challenge.ecommercebackend.modules.user.persisten.entity;

/**
 * Enum que representa el estado de un usuario.
 * Mapea directamente con el tipo auth.user_status de PostgreSQL.
 */
public enum UserStatus {
    ACTIVE("Activo"),
    INACTIVE("Inactivo"),
    SUSPENDED("Suspendido");

    private final String displayName;

    UserStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
