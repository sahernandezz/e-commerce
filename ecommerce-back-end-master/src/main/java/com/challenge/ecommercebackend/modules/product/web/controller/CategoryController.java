package com.challenge.ecommercebackend.modules.product.web.controller;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.service.ICategoryService;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class CategoryController {

    private final ICategoryService categoryService;

    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @QueryMapping("getAllCategoriesActive")
    public List<Category> getAllCategoriesActive() {
        return categoryService.getAllCategoriesActive();
    }
}
