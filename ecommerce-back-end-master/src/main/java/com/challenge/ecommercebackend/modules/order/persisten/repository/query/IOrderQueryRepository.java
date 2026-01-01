package com.challenge.ecommercebackend.modules.order.persisten.repository.query;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de LECTURA (Query) para órdenes.
 * Usa la vista materializada orders.orders_view que ya filtra órdenes canceladas.
 */
@Repository
public interface IOrderQueryRepository extends JpaRepository<Order, Long> {

    /**
     * Obtiene todas las órdenes activas.
     */
    @Query(value = "SELECT * FROM orders.orders_view", nativeQuery = true)
    List<Order> findAllActive();

    /**
     * Obtiene una orden por ID.
     */
    @Query(value = "SELECT * FROM orders.orders_view WHERE id = :id", nativeQuery = true)
    Optional<Order> findActiveById(@Param("id") Long id);

    /**
     * Obtiene órdenes por email del cliente.
     */
    @Query(value = "SELECT * FROM orders.orders_view WHERE email_customer = :email", nativeQuery = true)
    List<Order> findByEmailCustomer(@Param("email") String email);

    /**
     * Obtiene una orden por código.
     */
    @Query(value = "SELECT * FROM orders.orders_view WHERE order_code = :orderCode", nativeQuery = true)
    Optional<Order> findByOrderCode(@Param("orderCode") String orderCode);
}

