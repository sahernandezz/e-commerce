package com.challenge.ecommercebackend.modules.order.persisten.repository.command;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para órdenes.
 * Usa la tabla directa orders.orders para operaciones de INSERT, UPDATE, DELETE.
 */
@Repository
public interface IOrderCommandRepository extends JpaRepository<Order, Long> {

    /**
     * Busca una orden por código.
     */
    Optional<Order> findByOrderCode(String orderCode);
}

