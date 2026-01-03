package com.challenge.ecommercebackend.modules.order.domain.service;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    String createOrder(InputOrderRequest inputOrderRequest);

    // Cliente - Mis órdenes
    Page<Order> getOrdersByCustomerEmail(String email, Pageable pageable);
    List<Order> getOrdersByCustomerEmail(String email);
    Order getOrderByIdAndCustomerEmail(Long orderId, String email);
    void cancelOrderByCustomer(Long orderId, String email);

    // Admin - Gestión de órdenes
    Page<Order> getAllOrders(Pageable pageable, String status, String customerEmail);
    Order getOrderById(Long orderId);
    Order updateOrderStatus(Long orderId, OrderStatus status);
    void softDeleteOrder(Long orderId);
}

