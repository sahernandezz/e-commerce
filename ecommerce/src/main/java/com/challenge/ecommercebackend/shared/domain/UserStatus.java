package com.challenge.ecommercebackend.shared.domain;

/**
 * Estados estándar para usuarios.
 * Compartido entre backend y frontend via GraphQL.
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

