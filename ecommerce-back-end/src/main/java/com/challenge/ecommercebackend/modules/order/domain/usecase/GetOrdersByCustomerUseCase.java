package com.challenge.ecommercebackend.modules.order.domain.usecase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
public interface GetOrdersByCustomerUseCase {
    Page<Order> execute(String email, Pageable pageable);
    List<Order> execute(String email);
}
