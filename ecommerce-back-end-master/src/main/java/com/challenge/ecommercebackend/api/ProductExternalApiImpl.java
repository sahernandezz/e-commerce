package com.challenge.ecommercebackend.api;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.service.IProductService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class ProductExternalApiImpl implements IProductExternalApi {

    private final IProductService productService;

    public ProductExternalApiImpl(IProductService productService) {
        this.productService = productService;
    }

    public Map<String, Object> getProductById(Long id) {
        Optional<Product> product = productService.getProductById(id);

        if(product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Map<String, Object> productMap = new HashMap<>();
        product.ifPresent(value -> {
            productMap.put("name", value.getName());
            productMap.put("price", value.getPrice());
            productMap.put("discount", value.getDiscount());
        });
        return productMap;
    }
}
