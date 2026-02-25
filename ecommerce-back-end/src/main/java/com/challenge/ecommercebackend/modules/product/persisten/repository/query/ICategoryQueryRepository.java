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

@Repository
public interface ICategoryQueryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE c.status = 'ACTIVE' ORDER BY c.name")
    List<Category> findAllActive();

    @Query("SELECT c FROM Category c WHERE c.id = :id AND c.status = 'ACTIVE'")
    Optional<Category> findActiveById(@Param("id") Long id);

    Page<Category> findByStatus(CategoryStatus status, Pageable pageable);

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Category> findByStatusAndNameContainingIgnoreCase(CategoryStatus status, String name, Pageable pageable);

    Optional<Category> findByName(String name);
}

