package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.api.IProductExternalApi;
import com.challenge.ecommercebackend.email.IEmailSenderService;
import com.challenge.ecommercebackend.modules.order.domain.usecase.CreateOrderUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.entity.PaymentMethod;
import com.challenge.ecommercebackend.modules.order.persisten.entity.ProductOrder;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.web.dto.request.InputOrderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final IOrderCommandRepository orderCommandRepository;
    private final IProductExternalApi productExternalApi;
    private final IEmailSenderService emailSenderService;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public String execute(InputOrderRequest inputOrderRequest) {
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
}

