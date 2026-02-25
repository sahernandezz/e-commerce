package com.challenge.ecommercebackend.modules.admin.web.controller;

import com.challenge.ecommercebackend.modules.admin.domain.service.HomepageConfigService;
import com.challenge.ecommercebackend.modules.admin.domain.service.IAdminService;
import com.challenge.ecommercebackend.modules.admin.web.dto.DashboardStats;
import com.challenge.ecommercebackend.modules.admin.web.dto.HomepageConfigInput;
import com.challenge.ecommercebackend.modules.admin.web.dto.HomepageConfigResponse;
import com.challenge.ecommercebackend.modules.admin.web.dto.StatusOption;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import com.challenge.ecommercebackend.modules.order.web.dto.response.OrderResponse;
import com.challenge.ecommercebackend.modules.order.web.mapper.OrderMapper;
import com.challenge.ecommercebackend.modules.product.domain.service.IProductService;
import com.challenge.ecommercebackend.modules.product.persisten.entity.CategoryStatus;
import com.challenge.ecommercebackend.modules.product.persisten.entity.ProductStatus;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputCategoryRequest;
import com.challenge.ecommercebackend.modules.product.web.dto.request.InputProductRequest;
import com.challenge.ecommercebackend.modules.product.web.dto.response.CategoryResponse;
import com.challenge.ecommercebackend.modules.product.web.dto.response.ProductResponse;
import com.challenge.ecommercebackend.modules.product.web.mapper.ProductMapper;
import com.challenge.ecommercebackend.modules.user.domain.service.IUserService;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import com.challenge.ecommercebackend.modules.user.web.dto.response.UserResponse;
import com.challenge.ecommercebackend.modules.user.web.mapper.UserMapper;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminController {

    private final IAdminService adminService;
    private final IProductService productService;
    private final IOrderService orderService;
    private final IUserService userService;
    private final HomepageConfigService homepageConfigService;
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;
    private final UserMapper userMapper;

    // =============================================
    // Dashboard
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public DashboardStats getDashboardStats() {
        return adminService.getGetDashboardStatsUseCase().execute();
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<TopSellingProduct> getTopSellingProducts(@Argument Integer limit) {
        return adminService.getGetTopSellingProductsUseCase().execute(limit);
    }

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getRecentOrders(@Argument Integer limit) {
        return orderMapper.toResponseList(adminService.getGetRecentOrdersUseCase().execute(limit));
    }

    // =============================================
    // Products - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<ProductResponse> getProductsPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        var result = productService.getGetProductsUseCase().getAllPaginated(page, size, status, search);
        return PageResponse.<ProductResponse>builder()
                .content(productMapper.toResponseList(result.getContent()))
                .page(result.getPage())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .hasNext(result.isHasNext())
                .hasPrevious(result.isHasPrevious())
                .build();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse addProduct(@Argument InputProductRequest input) {
        return productMapper.toResponse(productService.getCreateProductUseCase().execute(input));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProduct(@Argument String id, @Argument InputProductRequest input) {
        return productMapper.toResponse(productService.getUpdateProductUseCase().execute(Long.parseLong(id), input));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse updateProductStatus(@Argument String id, @Argument String status) {
        return productMapper.toResponse(productService.getUpdateProductStatusUseCase().execute(Long.parseLong(id), status));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteProduct(@Argument String id) {
        productService.getDeleteProductUseCase().execute(Long.parseLong(id));
        return true;
    }

    // =============================================
    // Categories - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<CategoryResponse> getCategoriesPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        var result = productService.getGetCategoriesUseCase().getAllPaginated(page, size, status, search);
        return PageResponse.<CategoryResponse>builder()
                .content(productMapper.toCategoryResponseList(result.getContent()))
                .page(result.getPage())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .hasNext(result.isHasNext())
                .hasPrevious(result.isHasPrevious())
                .build();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(@Argument InputCategoryRequest input) {
        return productMapper.toCategoryResponse(productService.getCreateCategoryUseCase().execute(input.getName()));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(@Argument String id, @Argument InputCategoryRequest input) {
        return productMapper.toCategoryResponse(productService.getUpdateCategoryUseCase().execute(Long.parseLong(id), input.getName()));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategoryStatus(@Argument String id, @Argument String status) {
        return productMapper.toCategoryResponse(productService.getUpdateCategoryStatusUseCase().execute(Long.parseLong(id), status));
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Boolean deleteCategory(@Argument String id) {
        productService.getDeleteCategoryUseCase().execute(Long.parseLong(id));
        return true;
    }

    // =============================================
    // Orders - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public PageResponse<OrderResponse> getOrdersPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String customerEmail) {
        log.info("getOrdersPaginated called with page={}, size={}, status={}, customerEmail={}", page, size, status, customerEmail);
        try {
            var result = orderService.getGetOrdersUseCase().getAllPaginated(page, size, status, customerEmail);
            log.info("Orders fetched: {} orders", result.getContent().size());
            var response = PageResponse.<OrderResponse>builder()
                    .content(orderMapper.toResponseList(result.getContent()))
                    .page(result.getPage())
                    .size(result.getSize())
                    .totalElements(result.getTotalElements())
                    .totalPages(result.getTotalPages())
                    .first(result.isFirst())
                    .last(result.isLast())
                    .hasNext(result.isHasNext())
                    .hasPrevious(result.isHasPrevious())
                    .build();
            log.info("Response built successfully with {} orders", response.getContent().size());
            return response;
        } catch (Exception e) {
            log.error("Error al obtener órdenes", e);
            throw new RuntimeException("Error al obtener órdenes: " + e.getMessage(), e);
        }
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponse updateOrderStatus(@Argument String id, @Argument String status) {
        return orderMapper.toResponse(orderService.getUpdateOrderStatusUseCase().execute(Long.parseLong(id), status));
    }

    // =============================================
    // Users - Paginado
    // =============================================

    @QueryMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PageResponse<UserResponse> getUsersPaginated(
            @Argument int page,
            @Argument int size,
            @Argument String status,
            @Argument String search) {
        var result = userService.getGetUsersUseCase().getAllPaginated(page, size, status, search);
        return PageResponse.<UserResponse>builder()
                .content(userMapper.toResponseList(result.getContent()))
                .page(result.getPage())
                .size(result.getSize())
                .totalElements(result.getTotalElements())
                .totalPages(result.getTotalPages())
                .first(result.isFirst())
                .last(result.isLast())
                .hasNext(result.isHasNext())
                .hasPrevious(result.isHasPrevious())
                .build();
    }

    @MutationMapping
    @PreAuthorize("hasRole('ADMIN')")
    public UserResponse updateUserStatus(@Argument String id, @Argument String status) {
        return userMapper.toResponse(userService.getUpdateUserStatusUseCase().execute(Long.parseLong(id), status));
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

    // =============================================
    // Homepage Config
    // =============================================

    @QueryMapping("getHomepageConfig")
    public HomepageConfigResponse getHomepageConfig() {
        return homepageConfigService.getHomepageConfig();
    }

    @MutationMapping("updateHomepageConfig")
    @PreAuthorize("hasRole('ADMIN')")
    public HomepageConfigResponse updateHomepageConfig(@Argument("input") HomepageConfigInput input) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication != null ? authentication.getName() : "system";
        return homepageConfigService.updateHomepageConfig(input, username);
    }
}
