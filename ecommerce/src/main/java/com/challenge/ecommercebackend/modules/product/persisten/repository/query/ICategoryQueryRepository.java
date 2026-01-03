package com.challenge.ecommercebackend.modules.product.persisten.repository.query;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de LECTURA (Query) para categorías.
 * Usa la vista materializada catalog.category_view que ya filtra por status ACTIVE.
 */
@Repository
public interface ICategoryQueryRepository extends JpaRepository<Category, Long> {

    /**
     * Obtiene todas las categorías activas.
     */
    @Query(value = "SELECT * FROM catalog.category_view", nativeQuery = true)
    List<Category> findAllActive();

    /**
     * Obtiene una categoría por ID.
     */
    @Query(value = "SELECT * FROM catalog.category_view WHERE id = :id", nativeQuery = true)
    Optional<Category> findActiveById(@Param("id") Long id);

    // Admin methods
    Page<Category> findByStatus(CategoryStatus status, Pageable pageable);
}

