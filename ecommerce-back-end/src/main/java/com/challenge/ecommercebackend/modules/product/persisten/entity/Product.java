package com.challenge.ecommercebackend.modules.product.persisten.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(nullable = false, columnDefinition = "catalog.product_status")
    private ProductStatus status = ProductStatus.ACTIVE;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_images_url", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "images_url")
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_colors", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "colors")
    private List<String> colors;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_sizes", schema = "catalog", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "sizes")
    private List<String> sizes;
}
