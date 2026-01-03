package com.challenge.ecommercebackend.api;

import java.util.Map;

/**
 * API externa para el módulo de productos.
 * Permite comunicación desacoplada entre módulos.
 * En el futuro, esto puede convertirse en llamadas HTTP a un microservicio.
 */
public interface IProductExternalApi {

    Map<String, Object> getProductById(Long id);

    long countActiveProducts();
}
