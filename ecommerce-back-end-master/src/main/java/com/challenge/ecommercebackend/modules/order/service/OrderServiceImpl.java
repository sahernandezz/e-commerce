package com.challenge.ecommercebackend.modules.order.service;

import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.email.IEmailSenderService;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import com.challenge.ecommercebackend.modules.order.persisten.entity.ProductOrder;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderCommandRepository orderCommandRepository;
    private final IProductExternalApi productExternalApi;
    private final IEmailSenderService emailSenderService;

    public OrderServiceImpl(IOrderCommandRepository orderCommandRepository,
                           IProductExternalApi productExternalApi,
                           IEmailSenderService emailSenderService) {
        this.orderCommandRepository = orderCommandRepository;
        this.productExternalApi = productExternalApi;
        this.emailSenderService = emailSenderService;
    }

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
                    .emailCustomer(inputOrderRequest.getEmailCustomer())
                    .orderCode(UUID.randomUUID().toString() + "-" + new Date().getTime())
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
}

