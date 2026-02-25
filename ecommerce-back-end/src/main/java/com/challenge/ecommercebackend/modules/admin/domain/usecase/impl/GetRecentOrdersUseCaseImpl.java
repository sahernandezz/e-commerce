package com.challenge.ecommercebackend.modules.admin.domain.usecase.impl;

import com.challenge.ecommercebackend.api.IOrderExternalApi;
import com.challenge.ecommercebackend.modules.admin.domain.usecase.GetRecentOrdersUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetRecentOrdersUseCaseImpl implements GetRecentOrdersUseCase {

    private final IOrderExternalApi orderExternalApi;

    @Override
    public List<Order> execute(Integer limit) {
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

