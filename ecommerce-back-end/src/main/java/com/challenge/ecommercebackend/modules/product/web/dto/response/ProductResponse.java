package com.challenge.ecommercebackend.modules.product.web.dto.response;

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
    private Integer discount;
    private String status;
    private CategoryResponse category;
    private List<String> imagesUrl;
    private List<String> colors;
    private List<String> sizes;
    private Date createdAt;
    private Date updatedAt;
}

