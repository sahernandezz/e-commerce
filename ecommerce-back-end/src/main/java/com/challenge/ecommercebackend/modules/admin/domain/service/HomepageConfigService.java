package com.challenge.ecommercebackend.modules.admin.domain.service;

import com.challenge.ecommercebackend.modules.admin.persisten.entity.HomepageConfig;
import com.challenge.ecommercebackend.modules.admin.persisten.repository.query.IHomepageConfigQueryRepository;
import com.challenge.ecommercebackend.modules.admin.web.dto.HomepageConfigInput;
import com.challenge.ecommercebackend.modules.admin.web.dto.HomepageConfigResponse;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.repository.query.IProductQueryRepository;
import com.challenge.ecommercebackend.modules.product.web.dto.response.ProductResponse;
import com.challenge.ecommercebackend.modules.product.web.mapper.ProductMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HomepageConfigService {

    private final IHomepageConfigQueryRepository homepageConfigRepository;
    private final IProductQueryRepository productQueryRepository;
    private final ProductMapper productMapper;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(readOnly = true)
    public HomepageConfigResponse getHomepageConfig() {
        HomepageConfig config = homepageConfigRepository.findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Configuracion de homepage no encontrada"));

        return buildResponse(config);
    }

    @Transactional
    public HomepageConfigResponse updateHomepageConfig(HomepageConfigInput input, String username) {
        HomepageConfig config = homepageConfigRepository.findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Configuracion de homepage no encontrada"));

        if (input.getFeaturedProductMainId() != null) {
            config.setFeaturedProductMain(input.getFeaturedProductMainId());
        }
        if (input.getFeaturedProductSecondary1Id() != null) {
            config.setFeaturedProductSecondary1(input.getFeaturedProductSecondary1Id());
        }
        if (input.getFeaturedProductSecondary2Id() != null) {
            config.setFeaturedProductSecondary2(input.getFeaturedProductSecondary2Id());
        }

        if (input.getCarouselProductIds() != null) {
            config.setCarouselProductIds(input.getCarouselProductIds());
        }
        if (input.getShowCarousel() != null) {
            config.setShowCarousel(input.getShowCarousel());
        }
        if (input.getCarouselTitle() != null) {
            config.setCarouselTitle(input.getCarouselTitle());
        }

        if (input.getBannerTitle() != null) {
            config.setBannerTitle(input.getBannerTitle());
        }
        if (input.getBannerSubtitle() != null) {
            config.setBannerSubtitle(input.getBannerSubtitle());
        }
        if (input.getBannerImageUrl() != null) {
            config.setBannerImageUrl(input.getBannerImageUrl());
        }
        if (input.getBannerLink() != null) {
            config.setBannerLink(input.getBannerLink());
        }
        if (input.getBannerEnabled() != null) {
            config.setBannerEnabled(input.getBannerEnabled());
        }

        config.setUpdatedAt(new Date());
        config.setUpdatedBy(username);

        HomepageConfig saved = homepageConfigRepository.save(config);
        return buildResponse(saved);
    }

    private HomepageConfigResponse buildResponse(HomepageConfig config) {
        HomepageConfigResponse.HomepageConfigResponseBuilder builder = HomepageConfigResponse.builder()
                .id(config.getId())
                .showCarousel(config.getShowCarousel())
                .carouselTitle(config.getCarouselTitle())
                .bannerTitle(config.getBannerTitle())
                .bannerSubtitle(config.getBannerSubtitle())
                .bannerImageUrl(config.getBannerImageUrl())
                .bannerLink(config.getBannerLink())
                .bannerEnabled(config.getBannerEnabled())
                .updatedAt(config.getUpdatedAt() != null ? DATE_FORMAT.format(config.getUpdatedAt()) : null);

        if (config.getFeaturedProductMain() != null) {
            Optional<Product> product = productQueryRepository.findActiveById(config.getFeaturedProductMain());
            product.ifPresent(p -> builder.featuredProductMain(productMapper.toResponse(p)));
        }
        if (config.getFeaturedProductSecondary1() != null) {
            Optional<Product> product = productQueryRepository.findActiveById(config.getFeaturedProductSecondary1());
            product.ifPresent(p -> builder.featuredProductSecondary1(productMapper.toResponse(p)));
        }
        if (config.getFeaturedProductSecondary2() != null) {
            Optional<Product> product = productQueryRepository.findActiveById(config.getFeaturedProductSecondary2());
            product.ifPresent(p -> builder.featuredProductSecondary2(productMapper.toResponse(p)));
        }

        if (config.getCarouselProductIds() != null && !config.getCarouselProductIds().isEmpty()) {
            List<ProductResponse> carouselProducts = new ArrayList<>();
            for (Long productId : config.getCarouselProductIds()) {
                Optional<Product> product = productQueryRepository.findActiveById(productId);
                product.ifPresent(p -> carouselProducts.add(productMapper.toResponse(p)));
            }
            builder.carouselProducts(carouselProducts);
        } else {
            builder.carouselProducts(new ArrayList<>());
        }

        return builder.build();
    }
}

