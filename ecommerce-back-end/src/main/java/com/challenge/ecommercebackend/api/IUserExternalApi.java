package com.challenge.ecommercebackend.api;

/**
 * API externa para el módulo de usuarios.
 * Permite comunicación desacoplada entre módulos.
 * En el futuro, esto puede convertirse en llamadas HTTP a un microservicio.
 */
public interface IUserExternalApi {

    long countActiveUsers();
}

