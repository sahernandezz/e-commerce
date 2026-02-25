package com.challenge.ecommercebackend.modules.admin.domain.usecase.impl;

import com.challenge.ecommercebackend.api.IOrderExternalApi;
import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetTopSellingProductsUseCase;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetTopSellingProductsUseCaseImpl implements GetTopSellingProductsUseCase {

    private final IOrderExternalApi orderExternalApi;
    private final IProductExternalApi productExternalApi;

    @Override
    public List<TopSellingProduct> execute(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }

        List<Map<String, Object>> topProducts = orderExternalApi.getTopSellingProducts(limit);

        return topProducts.stream()
                .map(row -> {
                    Long productId = (Long) row.get("productId");
                    Integer totalSold = (Integer) row.get("totalSold");
                    Double revenue = (Double) row.get("revenue");

                    try {
                        Map<String, Object> productData = productExternalApi.getProductById(productId);

                        Product product = Product.builder()
                                .id(productId)
                                .name((String) productData.get("name"))
                                .price((Integer) productData.get("price"))
                                .discount((Integer) productData.get("discount"))
                                .build();

                        return TopSellingProduct.builder()
                                .product(product)
                                .totalSold(totalSold)
                                .revenue(revenue)
                                .build();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}

