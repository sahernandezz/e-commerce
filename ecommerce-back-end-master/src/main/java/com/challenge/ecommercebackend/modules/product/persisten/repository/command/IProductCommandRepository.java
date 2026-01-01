package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para productos.
 * Usa la tabla directa catalog.product para operaciones de INSERT, UPDATE, DELETE.
 */
@Repository
public interface IProductCommandRepository extends JpaRepository<Product, Long> {

    /**
     * Busca un producto por nombre (para validación de duplicados).
     */
    Optional<Product> findByName(String name);
}

