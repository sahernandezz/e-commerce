package com.challenge.ecommercebackend.modules.product.persisten.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product", schema = "catalog")
public class Product implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false, unique = true)
    private String name;

    @Column(length = 250)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status = ProductStatus.ACTIVE;

    @ElementCollection
    @CollectionTable(name = "product_images_url", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> imagesUrl;

    @Column(nullable = false)
    private Integer price;

    private Integer discount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @ToString.Exclude
    private Category category;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @ElementCollection
    @CollectionTable(name = "product_colors", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> colors;

    @ElementCollection
    @CollectionTable(name = "product_sizes", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    private List<String> sizes;
}
