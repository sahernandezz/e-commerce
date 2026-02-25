package com.challenge.ecommercebackend.modules.admin.persisten.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "homepage", schema = "config")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "featured_product_main")
    private Long featuredProductMain;

    @Column(name = "featured_product_secondary1")
    private Long featuredProductSecondary1;

    @Column(name = "featured_product_secondary2")
    private Long featuredProductSecondary2;

    @Column(name = "carousel_product_ids")
    @Convert(converter = LongArrayConverter.class)
    private List<Long> carouselProductIds;

    @Column(name = "banner_title")
    private String bannerTitle;

    @Column(name = "banner_subtitle")
    private String bannerSubtitle;

    @Column(name = "banner_image_url")
    private String bannerImageUrl;

    @Column(name = "banner_link")
    private String bannerLink;

    @Column(name = "banner_enabled")
    private Boolean bannerEnabled;

    @Column(name = "show_carousel")
    private Boolean showCarousel;

    @Column(name = "carousel_title")
    private String carouselTitle;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by")
    private String updatedBy;
}

