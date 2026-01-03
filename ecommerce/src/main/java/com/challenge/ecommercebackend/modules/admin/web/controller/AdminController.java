package com.challenge.ecommercebackend.modules.admin.web.controller;

import com.challenge.ecommercebackend.modules.admin.domain.service.IAdminService;
import com.challenge.ecommercebackend.modules.admin.web.dto.DashboardStats;
import com.challenge.ecommercebackend.modules.admin.web.dto.StatusOption;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrdersUseCase;
import com.challenge.ecommercebackend.modules.order.domain.usecase.UpdateOrderStatusUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import com.challenge.ecommercebackend.modules.product.domain.usecase.CreateCategoryUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.CreateProductUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.DeleteCategoryUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.DeleteProductUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.GetCategoriesUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.GetProductsUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.UpdateCategoryUseCase;
import com.challenge.ecommercebackend.modules.product.domain.usecase.UpdateProductUseCase;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Category;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputCategoryRequest;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import com.challenge.ecommercebackend.modules.user.domain.usecase.GetUsersUseCase;
import com.challenge.ecommercebackend.modules.user.domain.usecase.UpdateUserStatusUseCase;
import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;
    private final GetProductsUseCase getProductsUseCase;
    private final CreateProductUseCase createProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetCategoriesUseCase getCategoriesUseCase;
    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final GetOrdersUseCase getOrdersUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;
    private final GetUsersUseCase getUsersUseCase;
    private final UpdateUserStatusUseCase updateUserStatusUseCase;

    // =============================================
    // Dashboard
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardStats getDashboardStats() {
        return adminService.getDashboardStats();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TopSellingProduct> getTopSellingProducts(@Argument Integer limit) {
        return adminService.getTopSellingProducts(limit);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getRecentOrders(@Argument Integer limit) {
        return adminService.getRecentOrders(limit);
    }

    // =============================================
    // Products - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<Product> getProductsPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        return getProductsUseCase.getAllPaginated(page, size, status, search);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product addProduct(@Argument InputProductRequest input) {
        return createProductUseCase.execute(input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Product updateProduct(@Argument String id, @Argument InputProductRequest input) {
        return updateProductUseCase.execute(Long.parseLong(id), input);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteProduct(@Argument String id) {
        deleteProductUseCase.execute(Long.parseLong(id));
        return true;
    }

    // =============================================
    // Categories - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<Category> getCategoriesPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        return getCategoriesUseCase.getAllPaginated(page, size, status, search);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(@Argument InputCategoryRequest input) {
        return createCategoryUseCase.execute(input.getName());
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category updateCategory(@Argument String id, @Argument InputCategoryRequest input) {
        return updateCategoryUseCase.execute(Long.parseLong(id), input.getName());
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteCategory(@Argument String id) {
        deleteCategoryUseCase.execute(Long.parseLong(id));
        return true;
    }

    // =============================================
    // Orders - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<Order> getOrdersPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String customerEmail) {
        return getOrdersUseCase.getAllPaginated(page, size, status, customerEmail);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Order updateOrderStatus(@Argument String id, @Argument String status) {
        return updateOrderStatusUseCase.execute(Long.parseLong(id), status);
    }

    // =============================================
    // Users - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<User> getUsersPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        return getUsersUseCase.getAllPaginated(page, size, status, search);
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public User updateUserStatus(@Argument String id, @Argument String status) {
        return updateUserStatusUseCase.execute(Long.parseLong(id), status);
    }

    // =============================================
    // Status Options (para dropdowns del frontend)
    // =============================================

    @QueryMapping
    public List<StatusOption> getProductStatuses() {
        return Arrays.stream(ProductStatus.values())
                .map(s -> new StatusOption(s.name(), s.getDisplayName()))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<StatusOption> getCategoryStatuses() {
        return Arrays.stream(CategoryStatus.values())
                .map(s -> new StatusOption(s.name(), s.getDisplayName()))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<StatusOption> getOrderStatuses() {
        return Arrays.stream(OrderStatus.values())
                .map(s -> new StatusOption(s.name(), s.getDisplayName()))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<StatusOption> getUserStatuses() {
        return Arrays.stream(UserStatus.values())
                .map(s -> new StatusOption(s.name(), s.getDisplayName()))
                .collect(Collectors.toList());
    }

    @QueryMapping
    public List<StatusOption> getPaymentMethods() {
        return Arrays.stream(PaymentMethod.values())
                .map(s -> new StatusOption(s.name(), s.getDisplayName()))
                .collect(Collectors.toList());
    }
}
