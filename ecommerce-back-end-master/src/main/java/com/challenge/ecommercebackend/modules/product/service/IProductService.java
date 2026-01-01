package com.challenge.ecommercebackend.modules.product.service;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> getAllProducts(Pageable pageable);
    List<Product> getAllProductsActive();
    List<Product> getAllProductsActiveByCategoryId(Long categoryId);
    List<Product> getAllProductsActiveByName(String name);
    Product addProduct(InputProductRequest inputProductRequest);
    Optional<Product> getProductById(Long id);
    Optional<Product> updateProduct(Product product);
}
