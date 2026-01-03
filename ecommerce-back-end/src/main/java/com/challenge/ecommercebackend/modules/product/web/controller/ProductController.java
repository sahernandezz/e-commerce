package com.challenge.ecommercebackend.modules.product.web.controller;

import com.challenge.ecommercebackend.modules.product.domain.usecase.GetCategoriesUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.GetProductsUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
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

    private final GetProductsUseCase getProductsUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;

    @QueryMapping("getAllProductsActive")
    public List<Product> getAllProductsActive() {
        return getProductsUseCase.getAllActive();
    }

    @QueryMapping("getAllProductsActiveByCategoryId")
    public List<Product> getAllProductsActiveByCategoryId(@Argument("categoryId") String categoryId) {
        return getProductsUseCase.getActiveByCategoryId(Long.parseLong(categoryId));
    }

    @QueryMapping("getProductById")
    public Product getProductById(@Argument("id") String id) {
        return getProductsUseCase.getById(Long.parseLong(id));
    }

    @QueryMapping("getAllProductsActiveByName")
    public List<Product> getAllProductsActiveByName(@Argument("name") String name) {
        return getProductsUseCase.getActiveByName(name);
    }

    @QueryMapping("getAllCategoriesActive")
    public List<Category> getAllCategoriesActive() {
        return getCategoriesUseCase.getAllActive();
    }
}
