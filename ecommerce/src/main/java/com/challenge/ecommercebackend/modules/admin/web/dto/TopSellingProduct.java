package com.challenge.ecommercebackend.modules.admin.web.dto;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSellingProduct {
    private Product product;
    private Integer totalSold;
    private Double revenue;
}

