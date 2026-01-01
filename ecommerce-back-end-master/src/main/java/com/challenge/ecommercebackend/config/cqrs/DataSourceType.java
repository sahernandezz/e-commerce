package com.challenge.ecommercebackend.config.cqrs;

/**
 * Enum para identificar el tipo de DataSource en CQRS
 */
public enum DataSourceType {
    /**
     * DataSource para operaciones de escritura (INSERT, UPDATE, DELETE)
     */
    WRITE,

    /**
     * DataSource para operaciones de lectura (SELECT)
     * Usa vistas materializadas para soft delete
     */
    READ
}

