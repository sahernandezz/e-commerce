package com.challenge.ecommercebackend.modules.user.persisten.repository.command;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para usuarios.
 * Usa la tabla directa auth.customer para operaciones de INSERT, UPDATE, DELETE.
 */
@Repository
public interface IUserCommandRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email (necesario para validación y autenticación).
     */
    Optional<User> findByEmail(String email);
}

