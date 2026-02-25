package com.challenge.ecommercebackend.modules.product.domain.service.impl;

import com.challenge.ecommercebackend.modules.product.domain.service.IProductService;
import com.challenge.ecommercebackend.modules.product.domain.usecase.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final GetProductsUseCase getProductsUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final UpdateProductStatusUseCase updateProductStatusUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final UpdateCategoryStatusUseCase updateCategoryStatusUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @Override
    public GetProductsUseCase getGetProductsUseCase() {
        return getProductsUseCase;
    }

    @Override
    public CreateProductUseCase getCreateProductUseCase() {
        return createProductUseCase;
    }

    @Override
    public UpdateProductUseCase getUpdateProductUseCase() {
        return updateProductUseCase;
    }

    @Override
    public UpdateProductStatusUseCase getUpdateProductStatusUseCase() {
        return updateProductStatusUseCase;
    }

    @Override
    public DeleteProductUseCase getDeleteProductUseCase() {
        return deleteProductUseCase;
    }

    @Override
    public GetCategoriesUseCase getGetCategoriesUseCase() {
        return getCategoriesUseCase;
    }

    @Override
    public CreateCategoryUseCase getCreateCategoryUseCase() {
        return createCategoryUseCase;
    }

    @Override
    public UpdateCategoryUseCase getUpdateCategoryUseCase() {
        return updateCategoryUseCase;
    }

    @Override
    public UpdateCategoryStatusUseCase getUpdateCategoryStatusUseCase() {
        return updateCategoryStatusUseCase;
    }

    @Override
    public DeleteCategoryUseCase getDeleteCategoryUseCase() {
        return deleteCategoryUseCase;
    }
}

