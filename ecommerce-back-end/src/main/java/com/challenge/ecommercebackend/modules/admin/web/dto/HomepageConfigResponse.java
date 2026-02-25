package com.challenge.ecommercebackend.modules.admin.web.dto;

import com.challenge.ecommercebackend.modules.product.web.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageConfigResponse {
    private Long id;

    // Productos destacados
    private ProductResponse featuredProductMain;
    private ProductResponse featuredProductSecondary1;
    private ProductResponse featuredProductSecondary2;

    // Carrusel
    private List<ProductResponse> carouselProducts;
    private Boolean showCarousel;
    private String carouselTitle;

    // Banner
    private String bannerTitle;
    private String bannerSubtitle;
    private String bannerImageUrl;
    private String bannerLink;
    private Boolean bannerEnabled;

    private String updatedAt;
}

