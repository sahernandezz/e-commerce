package com.challenge.ecommercebackend.modules.admin.domain.service.impl;

import com.challenge.ecommercebackend.api.IOrderExternalApi;
import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.api.IUserExternalApi;
import com.challenge.ecommercebackend.modules.admin.domain.service.IAdminService;
import com.challenge.ecommercebackend.modules.admin.web.dto.DashboardStats;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.product.persisten.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio de administración que usa APIs externas para comunicarse con otros módulos.
 * Esto permite que en el futuro los módulos puedan escalar a microservicios.
 */
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements IAdminService {

    private final IProductExternalApi productExternalApi;
    private final IOrderExternalApi orderExternalApi;
    private final IUserExternalApi userExternalApi;

    @Override
    public DashboardStats getDashboardStats() {
        long totalProducts = productExternalApi.countActiveProducts();
        long totalOrders = orderExternalApi.countActiveOrders();
        long totalUsers = userExternalApi.countActiveUsers();
        long pendingOrders = orderExternalApi.countByStatus(OrderStatus.PENDING.name());

        Double totalRevenue = orderExternalApi.getTotalRevenueByStatus(OrderStatus.DELIVERED.name());
        Double averageOrderValue = totalOrders > 0 ? totalRevenue / totalOrders : 0.0;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date startOfMonth = calendar.getTime();

        long ordersThisMonth = orderExternalApi.countOrdersAfterDate(startOfMonth);
        long newUsersThisMonth = 0;

        return DashboardStats.builder()
                .totalProducts((int) totalProducts)
                .totalOrders((int) totalOrders)
                .totalUsers((int) totalUsers)
                .pendingOrders((int) pendingOrders)
                .totalRevenue(totalRevenue)
                .averageOrderValue(averageOrderValue)
                .ordersThisMonth((int) ordersThisMonth)
                .newUsersThisMonth((int) newUsersThisMonth)
                .build();
    }

    @Override
    public List<TopSellingProduct> getTopSellingProducts(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }

        List<Map<String, Object>> topProducts = orderExternalApi.getTopSellingProducts(limit);

        return topProducts.stream()
                .map(row -> {
                    Long productId = (Long) row.get("productId");
                    Integer totalSold = (Integer) row.get("totalSold");
                    Double revenue = (Double) row.get("revenue");

                    try {
                        Map<String, Object> productData = productExternalApi.getProductById(productId);

                        Product product = Product.builder()
                                .id(productId)
                                .name((String) productData.get("name"))
                                .price((Integer) productData.get("price"))
                                .discount((Integer) productData.get("discount"))
                                .build();

                        return TopSellingProduct.builder()
                                .product(product)
                                .totalSold(totalSold)
                                .revenue(revenue)
                                .build();
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> getRecentOrders(Integer limit) {
        if (limit == null || limit <= 0) {
            limit = 5;
        }

        List<Map<String, Object>> recentOrdersData = orderExternalApi.getRecentOrders(limit);

        return recentOrdersData.stream()
                .map(data -> Order.builder()
                        .id((Long) data.get("id"))
                        .orderCode((String) data.get("orderCode"))
                        .emailCustomer((String) data.get("emailCustomer"))
                        .status(OrderStatus.valueOf((String) data.get("status")))
                        .total((Integer) data.get("total"))
                        .createdAt((Date) data.get("createdAt"))
                        .build())
                .collect(Collectors.toList());
    }
}

