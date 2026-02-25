package com.challenge.ecommercebackend.modules.product.domain.service;
import com.challenge.ecommercebackend.modules.product.domain.usecase.*;
public interface IProductService {
    GetProductsUseCase getGetProductsUseCase();
    CreateProductUseCase getCreateProductUseCase();
    UpdateProductUseCase getUpdateProductUseCase();
    UpdateProductStatusUseCase getUpdateProductStatusUseCase();
    DeleteProductUseCase getDeleteProductUseCase();
    GetCategoriesUseCase getGetCategoriesUseCase();
    CreateCategoryUseCase getCreateCategoryUseCase();
    UpdateCategoryUseCase getUpdateCategoryUseCase();
    UpdateCategoryStatusUseCase getUpdateCategoryStatusUseCase();
    DeleteCategoryUseCase getDeleteCategoryUseCase();
}
