package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrdersUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.command.IOrderCommandRepository;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersUseCaseImpl implements GetOrdersUseCase {

    private final IOrderQueryRepository orderQueryRepository;
    private final IOrderCommandRepository orderCommandRepository;

    @Override
    public Order getById(Long id) {
        return orderQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
    }

    @Override
    public List<Order> getByCustomerEmail(String email) {
        return orderQueryRepository.findByEmailCustomerOrderByCreatedAtDesc(email);
    }

    @Override
    public PageResponse<Order> getAllPaginated(int page, int size, String status, String customerEmail) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage;

        if (status != null && customerEmail != null) {
            orderPage = orderCommandRepository.findByStatusAndEmailCustomerContaining(
                    OrderStatus.valueOf(status), customerEmail, pageable);
        } else if (status != null) {
            orderPage = orderCommandRepository.findByStatus(OrderStatus.valueOf(status), pageable);
        } else if (customerEmail != null) {
            orderPage = orderCommandRepository.findByEmailCustomerContaining(customerEmail, pageable);
        } else {
            orderPage = orderCommandRepository.findAll(pageable);
        }

        return PageResponse.<Order>builder()
                .content(orderPage.getContent())
                .page(orderPage.getNumber())
                .size(orderPage.getSize())
                .totalElements(orderPage.getTotalElements())
                .totalPages(orderPage.getTotalPages())
                .first(orderPage.isFirst())
                .last(orderPage.isLast())
                .hasNext(orderPage.hasNext())
                .hasPrevious(orderPage.hasPrevious())
                .build();
    }
}

