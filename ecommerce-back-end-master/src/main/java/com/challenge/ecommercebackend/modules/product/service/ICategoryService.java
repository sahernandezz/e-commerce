package com.challenge.ecommercebackend.modules.product.service;

import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategoriesActive();
}
