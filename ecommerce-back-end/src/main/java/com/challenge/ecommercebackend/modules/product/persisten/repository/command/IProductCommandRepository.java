package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para productos.
 * Usa la tabla directa catalog.product para operaciones de INSERT, UPDATE, DELETE.
 * También incluye consultas de admin que necesitan acceso a todas las columnas.
 */
@Repository
public interface IProductCommandRepository extends JpaRepository<Product, Long> {

    /**
     * Busca un producto por nombre (para validación de duplicados).
     */
    Optional<Product> findByName(String name);

    // Admin methods - consultan la tabla directa para tener acceso a todas las columnas
    Page<Product> findByStatus(ProductStatus status, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByStatusAndNameContainingIgnoreCase(ProductStatus status, String name, Pageable pageable);

    long countByStatus(ProductStatus status);
}

