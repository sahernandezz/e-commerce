package com.challenge.ecommercebackend.modules.product.persisten.repository.query;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductQueryRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE p.status = 'ACTIVE' AND c.status = 'ACTIVE' " +
           "ORDER BY p.createdAt DESC")
    List<Product> findAllActive();

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE p.id = :id AND p.status = 'ACTIVE' AND c.status = 'ACTIVE'")
    Optional<Product> findActiveById(@Param("id") Long id);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE p.category.id = :categoryId AND p.status = 'ACTIVE' AND c.status = 'ACTIVE' " +
           "ORDER BY p.createdAt DESC")
    List<Product> findAllByCategoryId(@Param("categoryId") Long categoryId);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND p.status = 'ACTIVE' AND c.status = 'ACTIVE' " +
           "ORDER BY p.createdAt DESC")
    List<Product> findAllByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE p.status = :status " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findByStatus(@Param("status") ProductStatus status, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "WHERE p.status = :status " +
           "AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findByStatusAndNameContainingIgnoreCase(@Param("status") ProductStatus status, @Param("name") String name, Pageable pageable);

    @Query("SELECT DISTINCT p FROM Product p " +
           "LEFT JOIN FETCH p.category c " +
           "ORDER BY p.createdAt DESC")
    Page<Product> findAll(Pageable pageable);
}

