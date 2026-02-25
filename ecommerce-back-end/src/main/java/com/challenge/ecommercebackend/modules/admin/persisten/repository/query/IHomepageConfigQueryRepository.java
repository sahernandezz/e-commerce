package com.challenge.ecommercebackend.modules.admin.persisten.repository.query;

import com.challenge.ecommercebackend.modules.admin.persisten.entity.HomepageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHomepageConfigQueryRepository extends JpaRepository<HomepageConfig, Long> {

    @Query("SELECT h FROM HomepageConfig h ORDER BY h.id ASC")
    Optional<HomepageConfig> findFirst();

    @Modifying
    @Query(value = "UPDATE config.homepage SET carousel_product_ids = :productIds WHERE id = :id", nativeQuery = true)
    void updateCarouselProductIds(@Param("id") Long id, @Param("productIds") Long[] productIds);
}

