package com.challenge.ecommercebackend.modules.user.persisten.repository.command;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import com.challenge.ecommercebackend.modules.user.persisten.entity.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para usuarios.
 * Usa la tabla directa auth.customer para operaciones de INSERT, UPDATE, DELETE.
 * También incluye consultas de admin que necesitan acceso a todas las columnas.
 */
@Repository
public interface IUserCommandRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email (necesario para validación y autenticación).
     */
    Optional<User> findByEmail(String email);

    // Admin/Dashboard methods
    long countByStatus(UserStatus status);

    // Admin filter methods
    Page<User> findByStatus(UserStatus status, Pageable pageable);

    Page<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email, Pageable pageable);

    Page<User> findByStatusAndNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            UserStatus status, String name, String email, Pageable pageable);
}

