package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para categorías.
 * Usa la tabla directa catalog.category para operaciones de INSERT, UPDATE, DELETE.
 */
@Repository
public interface ICategoryCommandRepository extends JpaRepository<Category, Long> {

    /**
     * Busca una categoría por nombre (para validación de duplicados).
     */
    Optional<Category> findByName(String name);
}

