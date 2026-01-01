package com.challenge.ecommercebackend.modules.product.persisten.repository.query;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de LECTURA (Query) para productos.
 * Usa la vista materializada catalog.product_view que ya filtra por status ACTIVE.
 * NO incluir filtros por estado ya que la vista ya lo hace.
 */
@Repository
public interface IProductQueryRepository extends JpaRepository<Product, Long> {

    /**
     * Obtiene todos los productos activos.
     */
    @Query(value = "SELECT * FROM catalog.product_view", nativeQuery = true)
    List<Product> findAllActive();

    /**
     * Obtiene un producto por ID.
     */
    @Query(value = "SELECT * FROM catalog.product_view WHERE id = :id", nativeQuery = true)
    Optional<Product> findActiveById(@Param("id") Long id);

    /**
     * Obtiene productos por categoría.
     */
    @Query(value = "SELECT * FROM catalog.product_view WHERE category_id = :categoryId", nativeQuery = true)
    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Busca productos por nombre (case insensitive).
     */
    @Query(value = "SELECT * FROM catalog.product_view WHERE LOWER(name) LIKE LOWER(CONCAT('%', :name, '%'))", nativeQuery = true)
    List<Product> findAllByNameContainingIgnoreCase(@Param("name") String name);
}

