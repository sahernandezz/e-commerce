package com.challenge.ecommercebackend.modules.order.persisten.repository.command;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Repositorio de ESCRITURA (Command) para órdenes.
 * Usa la tabla directa orders.orders para operaciones de INSERT, UPDATE, DELETE.
 * También incluye consultas de admin que necesitan acceso a todas las columnas.
 */
@Repository
public interface IOrderCommandRepository extends JpaRepository<Order, Long> {

    // Admin/Dashboard methods
    long countByStatus(OrderStatus status);

    long countByStatusNot(OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    Double sumTotalByStatus(@Param("status") OrderStatus status);

    long countByCreatedAtAfterAndStatusNot(Date date, OrderStatus status);

    @Query("SELECT op.idProduct, SUM(op.quantity), SUM(op.total) " +
           "FROM ProductOrder op " +
           "JOIN op.order o " +
           "WHERE o.status != 'CANCELED' " +
           "GROUP BY op.idProduct " +
           "ORDER BY SUM(op.quantity) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // Admin filter methods
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByEmailCustomerContaining(String email, Pageable pageable);

    Page<Order> findByStatusAndEmailCustomerContaining(OrderStatus status, String email, Pageable pageable);
}

