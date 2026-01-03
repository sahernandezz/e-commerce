package com.challenge.ecommercebackend.modules.order.persisten.repository.query;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    List<Order> findByEmailCustomerList(@Param("email") String email);

    /**
     * Obtiene una orden por código.
     */
    @Query(value = "SELECT * FROM orders.orders_view WHERE order_code = :orderCode", nativeQuery = true)
    Optional<Order> findByOrderCode(@Param("orderCode") String orderCode);

    // Customer methods
    Page<Order> findByEmailCustomer(String email, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE LOWER(o.emailCustomer) = LOWER(:email) ORDER BY o.createdAt DESC")
    List<Order> findByEmailCustomerOrderByCreatedAtDesc(@Param("email") String email);

    Optional<Order> findByIdAndEmailCustomer(Long id, String email);

    // Admin methods
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    Page<Order> findByEmailCustomerContaining(String email, Pageable pageable);

    Page<Order> findByStatusAndEmailCustomerContaining(OrderStatus status, String email, Pageable pageable);

    // Dashboard methods
    long countByStatus(OrderStatus status);

    long countByStatusNot(OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    Double sumTotalByStatus(@Param("status") OrderStatus status);

    long countByCreatedAtAfterAndStatusNot(java.util.Date date, OrderStatus status);

    @Query("SELECT op.idProduct, SUM(op.quantity), SUM(op.total) " +
           "FROM ProductOrder op " +
           "JOIN op.order o " +
           "WHERE o.status != 'CANCELED' " +
           "GROUP BY op.idProduct " +
           "ORDER BY SUM(op.quantity) DESC")
    List<Object[]> findTopSellingProducts(Pageable pageable);

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);
}

