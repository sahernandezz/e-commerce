package com.challenge.ecommercebackend.modules.product.web.controller;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.service.IProductService;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ProductController {

    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @QueryMapping("getAllProductsActive")
    public List<Product> getAllProductsActive() {
        return productService.getAllProductsActive();
    }

    @QueryMapping("getAllProductsActiveByCategoryId")
    public List<Product> getAllProductsActiveByCategoryId(@Argument("categoryId") String categoryId) {
        final Long categoryIdLong = Long.parseLong(categoryId);
        return productService.getAllProductsActiveByCategoryId(categoryIdLong);
    }

    @QueryMapping("getProductById")
    public Product getProductById(@Argument("id") String id) {
        final Long productId = Long.parseLong(id);
        return productService.getProductById(productId).orElse(null);
    }

    @MutationMapping("addProduct")
    public Product addProduct(@Argument InputProductRequest input) {
        return productService.addProduct(input);
    }

    @QueryMapping("getAllProductsActiveByName")
    public List<Product> getAllProductsActiveByName(String name) {
        return productService.getAllProductsActiveByName(name);
    }
}
