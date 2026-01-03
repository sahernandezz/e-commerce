package com.challenge.ecommercebackend.api;

import java.util.List;
import java.util.Map;

/**
 * API externa para el módulo de órdenes.
 * Permite comunicación desacoplada entre módulos.
 * En el futuro, esto puede convertirse en llamadas HTTP a un microservicio.
 */
public interface IOrderExternalApi {

    long countActiveOrders();

    long countByStatus(String status);

    Double getTotalRevenueByStatus(String status);

    long countOrdersAfterDate(java.util.Date date);

    List<Map<String, Object>> getTopSellingProducts(int limit);

    List<Map<String, Object>> getRecentOrders(int limit);
}

