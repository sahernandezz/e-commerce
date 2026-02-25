package com.challenge.ecommercebackend.modules.admin.domain.usecase.impl;

import com.challenge.ecommercebackend.api.IOrderExternalApi;
import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.api.IUserExternalApi;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetDashboardStatsUseCase;
import com.challenge.ecommercebackend.modules.admin.web.dto.DashboardStats;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class GetDashboardStatsUseCaseImpl implements GetDashboardStatsUseCase {

    private final IProductExternalApi productExternalApi;
    private final IOrderExternalApi orderExternalApi;
    private final IUserExternalApi userExternalApi;

    @Override
    public DashboardStats execute() {
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
}

