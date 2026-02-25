package com.challenge.ecommercebackend.modules.order.persisten.repository.query.projection;

import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;

import java.util.Date;

/**
 * Proyección para órdenes recientes del dashboard.
 */
public interface RecentOrderProjection {
    Long getId();
    String getOrderCode();
    String getEmailCustomer();
    OrderStatus getStatus();
    Integer getTotal();
    Date getCreatedAt();
}

