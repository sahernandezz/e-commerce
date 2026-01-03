package com.challenge.ecommercebackend.modules.order.domain.service.impl;

import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.email.IEmailSenderService;
import com.challenge.ecommercebackend.modules.order.domain.service.IOrderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import com.challenge.ecommercebackend.modules.order.persisten.entity.ProductOrder;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements IOrderService {

    private final IOrderCommandRepository orderCommandRepository;
    private final IOrderQueryRepository orderQueryRepository;
    private final IProductExternalApi productExternalApi;
    private final IEmailSenderService emailSenderService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String createOrder(InputOrderRequest inputOrderRequest) {
        try {
            List<ProductOrder> productOrderList = new ArrayList<>();
            inputOrderRequest.getProducts().forEach(productRequest -> {
                Map<String, Object> product = productExternalApi.getProductById(productRequest.getProductId());
                ProductOrder productOrder = ProductOrder.builder()
                        .idProduct(productRequest.getProductId())
                        .color(productRequest.getColor())
                        .size(productRequest.getSize())
                        .quantity(productRequest.getQuantity())
                        .unitPrice((Integer) product.get("price"))
                        .name((String) product.get("name"))
                        .discount((Integer) product.get("discount"))
                        .total((Integer) product.get("price") * productRequest.getQuantity())
                        .build();
                productOrderList.add(productOrder);
            });

            Order order = Order.builder()
                    .createdAt(new Date())
                    .address(inputOrderRequest.getAddress())
                    .city(inputOrderRequest.getCity())
                    .description(inputOrderRequest.getDescription())
                    .emailCustomer(inputOrderRequest.getEmailCustomer().toLowerCase().trim())
                    .orderCode(UUID.randomUUID() + "-" + new Date().getTime())
                    .paymentMethod(PaymentMethod.valueOf(inputOrderRequest.getPaymentMethod()))
                    .total(productOrderList.stream().mapToInt(ProductOrder::getTotal).sum())
                    .status(OrderStatus.PENDING)
                    .build();

            Order result = orderCommandRepository.save(order);
            productOrderList.forEach(productOrder -> productOrder.setOrder(result));
            result.setProducts(productOrderList);
            orderCommandRepository.save(result);
            emailSenderService.sendEmail(result.getEmailCustomer(), "Order created successfully", "Order created successfully");
            return "Order created successfully";
        } catch (Exception e) {
            return "Error creating order";
        }
    }

    @Override
    public Page<Order> getOrdersByCustomerEmail(String email, Pageable pageable) {
        return orderQueryRepository.findByEmailCustomer(email, pageable);
    }

    @Override
    public List<Order> getOrdersByCustomerEmail(String email) {
        return orderQueryRepository.findByEmailCustomerOrderByCreatedAtDesc(email);
    }

    @Override
    public Order getOrderByIdAndCustomerEmail(Long orderId, String email) {
        return orderQueryRepository.findByIdAndEmailCustomer(orderId, email)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
    }

    @Override
    @Transactional
    public void cancelOrderByCustomer(Long orderId, String email) {
        Order order = getOrderByIdAndCustomerEmail(orderId, email);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Solo se pueden cancelar órdenes pendientes");
        }
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Date());
        orderCommandRepository.save(order);
    }

    @Override
    public Page<Order> getAllOrders(Pageable pageable, String status, String customerEmail) {
        if (status != null && customerEmail != null) {
            return orderCommandRepository.findByStatusAndEmailCustomerContaining(
                    OrderStatus.valueOf(status), customerEmail, pageable);
        } else if (status != null) {
            return orderCommandRepository.findByStatus(OrderStatus.valueOf(status), pageable);
        } else if (customerEmail != null) {
            return orderCommandRepository.findByEmailCustomerContaining(customerEmail, pageable);
        }
        return orderCommandRepository.findAll(pageable);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderQueryRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
    }

    @Override
    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus status) {
        Order order = orderCommandRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
        order.setStatus(status);
        order.setUpdatedAt(new Date());
        return orderCommandRepository.save(order);
    }

    @Override
    @Transactional
    public void softDeleteOrder(Long orderId) {
        Order order = orderCommandRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
        order.setStatus(OrderStatus.CANCELED);
        order.setUpdatedAt(new Date());
        orderCommandRepository.save(order);
    }
}

