package com.challenge.ecommercebackend.modules.order.persisten.repository.query;

import com.challenge.ecommercebackend.modules.order.persisten.entity.Order;
import com.challenge.ecommercebackend.modules.order.persisten.entity.OrderStatus;
import com.challenge.ecommercebackend.modules.order.persisten.repository.query.projection.RecentOrderProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOrderQueryRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products",
           countQuery = "SELECT COUNT(o) FROM Order o")
    Page<Order> findAllWithProducts(Pageable pageable);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products WHERE o.status = :status",
           countQuery = "SELECT COUNT(o) FROM Order o WHERE o.status = :status")
    Page<Order> findByStatusWithProducts(@Param("status") OrderStatus status, Pageable pageable);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products WHERE o.emailCustomer LIKE %:email%",
           countQuery = "SELECT COUNT(o) FROM Order o WHERE o.emailCustomer LIKE %:email%")
    Page<Order> findByEmailCustomerContainingWithProducts(@Param("email") String email, Pageable pageable);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products WHERE o.status = :status AND o.emailCustomer LIKE %:email%",
           countQuery = "SELECT COUNT(o) FROM Order o WHERE o.status = :status AND o.emailCustomer LIKE %:email%")
    Page<Order> findByStatusAndEmailCustomerContainingWithProducts(@Param("status") OrderStatus status, @Param("email") String email, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.products WHERE LOWER(o.emailCustomer) = LOWER(:email) ORDER BY o.createdAt DESC")
    List<Order> findByEmailCustomerOrderByCreatedAtDesc(@Param("email") String email);

    Page<Order> findByEmailCustomer(String email, Pageable pageable);

    Optional<Order> findByIdAndEmailCustomer(Long id, String email);

    long countByStatus(OrderStatus status);

    long countByStatusNot(OrderStatus status);

    @Query("SELECT SUM(o.total) FROM Order o WHERE o.status = :status")
    Double sumTotalByStatus(@Param("status") OrderStatus status);

    long countByCreatedAtAfterAndStatusNot(java.util.Date date, OrderStatus status);

    @Query("""
           SELECT op.idProduct, SUM(op.quantity), SUM(op.total)
               FROM ProductOrder op
                   JOIN op.order o
                   WHERE o.status != 'CANCELED'
                   GROUP BY op.idProduct
                   ORDER BY SUM(op.quantity) DESC
           """)
    List<Object[]> findTopSellingProducts(Pageable pageable);

    Page<Order> findAllByOrderByCreatedAtDesc(Pageable pageable);

    @Query("SELECT o.id AS id, o.orderCode AS orderCode, o.emailCustomer AS emailCustomer, " +
           "o.status AS status, o.total AS total, o.createdAt AS createdAt " +
           "FROM Order o ORDER BY o.createdAt DESC")
    List<RecentOrderProjection> findRecentOrdersProjection(Pageable pageable);
}

