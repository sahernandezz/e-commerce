package com.challenge.ecommercebackend.modules.product.web.dto.response;

import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private ProductStatus status;
    private Long categoryId;
    private String categoryName;
    private List<String> imagesUrl;
    private List<String> colors;
    private List<String> sizes;
    private Date createdAt;
    private Date updatedAt;
}

