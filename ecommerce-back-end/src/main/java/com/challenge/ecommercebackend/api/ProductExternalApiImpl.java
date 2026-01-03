package com.challenge.ecommercebackend.api;

import com.challenge.ecommercebackend.modules.product.domain.usecase.GetProductsUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductExternalApiImpl implements IProductExternalApi {

    private final GetProductsUseCase getProductsUseCase;

    @Override
    public Map<String, Object> getProductById(Long id) {
        Product product = getProductsUseCase.getById(id);

        Map<String, Object> productMap = new HashMap<>();
        productMap.put("name", product.getName());
        productMap.put("price", product.getPrice());
        productMap.put("discount", product.getDiscount());
        return productMap;
    }

    @Override
    public long countActiveProducts() {
        return getProductsUseCase.getAllActive().size();
    }
}
