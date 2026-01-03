package com.challenge.ecommercebackend.modules.admin.domain.service;

import com.challenge.ecommercebackend.modules.admin.web.dto.DashboardStats;
import com.challenge.ecommercebackend.modules.admin.web.dto.TopSellingProduct;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;

import java.util.List;

public interface IAdminService {
    DashboardStats getDashboardStats();
    List<TopSellingProduct> getTopSellingProducts(Integer limit);
    List<Order> getRecentOrders(Integer limit);
}

