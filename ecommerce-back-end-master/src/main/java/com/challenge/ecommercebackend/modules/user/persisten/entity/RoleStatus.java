package com.challenge.ecommercebackend.modules.user.persisten.entity;

/**
 * Enum que representa el estado de un rol.
 * Mapea directamente con el tipo auth.role_status de PostgreSQL.
 */
public enum RoleStatus {
    ACTIVE,
    INACTIVE
}
