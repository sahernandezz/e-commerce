package com.challenge.ecommercebackend.modules.admin.domain.usecase;
import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import java.util.List;
public interface GetRecentOrdersUseCase {
    List<Order> execute(Integer limit);
}
