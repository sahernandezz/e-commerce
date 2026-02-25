package com.challenge.ecommercebackend.modules.order.domain.usecase.impl;

import com.challenge.ecommercebackend.modules.order.domain.usecase.GetOrdersUseCase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.IOrderQueryRepository;
import com.challenge.ecommercebackend.shared.domain.PageResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrdersUseCaseImpl implements GetOrdersUseCase {

    private final IOrderQueryRepository orderQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderQueryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getByCustomerEmail(String email) {
        return orderQueryRepository.findByEmailCustomerOrderByCreatedAtDesc(email);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<Order> getAllPaginated(int page, int size, String status, String customerEmail) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Order> orderPage;

        if (status != null && customerEmail != null) {
            orderPage = orderQueryRepository.findByStatusAndEmailCustomerContainingWithProducts(
                    OrderStatus.valueOf(status), customerEmail, pageable);
        } else if (status != null) {
            orderPage = orderQueryRepository.findByStatusWithProducts(OrderStatus.valueOf(status), pageable);
        } else if (customerEmail != null) {
            orderPage = orderQueryRepository.findByEmailCustomerContainingWithProducts(customerEmail, pageable);
        } else {
            orderPage = orderQueryRepository.findAllWithProducts(pageable);
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

