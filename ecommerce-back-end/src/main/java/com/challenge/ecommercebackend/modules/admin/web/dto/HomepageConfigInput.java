package com.challenge.ecommercebackend.modules.admin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageConfigInput {
    // Productos destacados (IDs)
    private Long featuredProductMainId;
    private Long featuredProductSecondary1Id;
    private Long featuredProductSecondary2Id;

    // Carrusel (IDs de productos)
    private List<Long> carouselProductIds;
    private Boolean showCarousel;
    private String carouselTitle;

    // Banner
    private String bannerTitle;
    private String bannerSubtitle;
    private String bannerImageUrl;
    private String bannerLink;
    private Boolean bannerEnabled;
}

