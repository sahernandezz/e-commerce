package com.challenge.ecommercebackend.modules.admin.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStats {
    private Integer totalProducts;
    private Integer totalOrders;
    private Integer totalUsers;
    private Integer pendingOrders;
    private Double totalRevenue;
    private Double averageOrderValue;
    private Integer ordersThisMonth;
    private Integer newUsersThisMonth;
}
