package com.challenge.ecommercebackend.modules.user.persisten.entity;

/**
 * Enum centralizado para los nombres de roles del sistema.
 * Mantiene consistencia entre backend y frontend.
 */
public enum RoleName {
    ADMIN("ADMIN", "Administrador"),
    USER("USER", "Cliente");

    private final String code;
    private final String displayName;

    RoleName(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Obtiene el RoleName a partir de un código de string.
     * @param code El código del rol (ej: "ADMIN", "USER")
     * @return El RoleName correspondiente
     * @throws IllegalArgumentException si el código no existe
     */
    public static RoleName fromCode(String code) {
        for (RoleName role : values()) {
            if (role.getCode().equalsIgnoreCase(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Rol no encontrado: " + code);
    }

    /**
     * Verifica si un código de rol es válido.
     * @param code El código a verificar
     * @return true si es válido, false en caso contrario
     */
    public static boolean isValid(String code) {
        try {
            fromCode(code);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Verifica si este rol es administrador.
     * @return true si es ADMIN
     */
    public boolean isAdmin() {
        return this == ADMIN;
    }

    /**
     * Verifica si este rol es usuario regular.
     * @return true si es USER
     */
    public boolean isUser() {
        return this == USER;
    }
}

