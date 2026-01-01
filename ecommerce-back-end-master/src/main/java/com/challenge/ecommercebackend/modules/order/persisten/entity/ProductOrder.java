package com.challenge.ecommercebackend.modules.order.persisten.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_product", schema = "orders")
public class ProductOrder implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_order", nullable = false)
    private Order order;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long idProduct;

    private String color;

    private String size;

    @Column(nullable = false)
    private Integer unitPrice;

    private Integer discount;

    @Column(nullable = false)
    private Integer total;
}
