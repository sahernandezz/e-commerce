package com.challenge.ecommercebackend.modules.product.persisten.repository.command;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para categorías.
 * Usa la tabla directa catalog.category para operaciones de INSERT, UPDATE, DELETE.
 */
@Repository
public interface ICategoryCommandRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    // Admin methods
    Page<Category> findByStatus(CategoryStatus status, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findByStatusAndNameContainingIgnoreCase(CategoryStatus status, String name, Pageable pageable);
}

