package com.challenge.ecommercebackend.modules.product.web.controller;

import com.challenge.ecommercebackend.modules.product.domain.service.IProductService;
import com.challenge.ecommercebackend.modules.product.web.dto.response.CategoryResponse;
import com.challenge.ecommercebackend.modules.product.web.dto.response.ProductResponse;
import com.challenge.ecommercebackend.modules.product.web.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Controller para queries públicas de productos y categorías.
 * Las mutaciones están en AdminController.
 */
@Controller
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;
    private final ProductMapper productMapper;

    @QueryMapping("getAllProductsActive")
    public List<ProductResponse> getAllProductsActive() {
        return productMapper.toResponseList(productService.getGetProductsUseCase().getAllActive());
    }

    @QueryMapping("getAllProductsActiveByCategoryId")
    public List<ProductResponse> getAllProductsActiveByCategoryId(@Argument("categoryId") String categoryId) {
        return productMapper.toResponseList(productService.getGetProductsUseCase().getActiveByCategoryId(Long.parseLong(categoryId)));
    }

    @QueryMapping("getProductById")
    public ProductResponse getProductById(@Argument("id") String id) {
        return productMapper.toResponse(productService.getGetProductsUseCase().getById(Long.parseLong(id)));
    }

    @QueryMapping("getAllProductsActiveByName")
    public List<ProductResponse> getAllProductsActiveByName(@Argument("name") String name) {
        return productMapper.toResponseList(productService.getGetProductsUseCase().getActiveByName(name));
    }

    @QueryMapping("getAllCategoriesActive")
    public List<CategoryResponse> getAllCategoriesActive() {
        return productMapper.toCategoryResponseList(productService.getGetCategoriesUseCase().getAllActive());
    }
}
