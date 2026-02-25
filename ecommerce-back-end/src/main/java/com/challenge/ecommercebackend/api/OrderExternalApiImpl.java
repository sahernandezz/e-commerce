package com.challenge.ecommercebackend.api;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.projection.RecentOrderProjection;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class OrderExternalApiImpl implements IOrderExternalApi {

    private final IOrderQueryRepository orderQueryRepository;

    public OrderExternalApiImpl(IOrderQueryRepository orderQueryRepository) {
        this.orderQueryRepository = orderQueryRepository;
    }

    @Override
    public long countActiveOrders() {
        return orderQueryRepository.countByStatusNot(OrderStatus.CANCELED);
    }

    @Override
    public long countByStatus(String status) {
        return orderQueryRepository.countByStatus(OrderStatus.valueOf(status));
    }

    @Override
    public Double getTotalRevenueByStatus(String status) {
        Double revenue = orderQueryRepository.sumTotalByStatus(OrderStatus.valueOf(status));
        return revenue != null ? revenue : 0.0;
    }

    @Override
    public long countOrdersAfterDate(Date date) {
        return orderQueryRepository.countByCreatedAtAfterAndStatusNot(date, OrderStatus.CANCELED);
    }

    @Override
    public List<Map<String, Object>> getTopSellingProducts(int limit) {
        List<Object[]> results = orderQueryRepository.findTopSellingProducts(PageRequest.of(0, limit));

        return results.stream()
                .map(row -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("productId", ((Number) row[0]).longValue());
                    map.put("totalSold", ((Number) row[1]).intValue());
                    map.put("revenue", ((Number) row[2]).doubleValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getRecentOrders(int limit) {
        List<RecentOrderProjection> orders = orderQueryRepository.findRecentOrdersProjection(
                PageRequest.of(0, limit)
        );

        return orders.stream()
                .map(order -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", order.getId());
                    map.put("orderCode", order.getOrderCode());
                    map.put("emailCustomer", order.getEmailCustomer());
                    map.put("status", order.getStatus().name());
                    map.put("total", order.getTotal());
                    map.put("createdAt", order.getCreatedAt());
                    return map;
                })
                .collect(Collectors.toList());
    }
}

