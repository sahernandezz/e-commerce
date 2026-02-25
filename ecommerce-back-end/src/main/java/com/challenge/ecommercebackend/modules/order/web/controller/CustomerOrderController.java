package com.challenge.ecommercebackend.modules.order.web.controller;

import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.web.dto.response.OrderResponse;
import com.challenge.ecommercebackend.modules.order.web.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestión de órdenes del cliente.
 */
@RestController
@RequestMapping("/api/v1/my-orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final IOrderService orderService;
    private final OrderMapper orderMapper;

    /**
     * Obtener órdenes del usuario autenticado.
     */
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String userEmail = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<Order> orders = orderService.getGetOrdersByCustomerUseCase().execute(userEmail, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * Obtener detalle de una orden específica del usuario.
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getMyOrderDetail(
            @PathVariable Long orderId,
            Authentication authentication) {

        String userEmail = authentication.getName();
        Order order = orderService.getGetOrderByIdAndCustomerUseCase().execute(orderId, userEmail);

        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    /**
     * Cancelar una orden (solo si está en estado PENDING).
     */
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        String userEmail = authentication.getName();
        orderService.getCancelOrderByCustomerUseCase().execute(orderId, userEmail);

        return ResponseEntity.ok().build();
    }
}

