package com.challenge.ecommercebackend.modules.user.persisten.repository.command;

import com.challenge.ecommercebackend.modules.user.persisten.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de ESCRITURA (Command) para roles.
 * Usa la tabla directa auth.role.
 */
@Repository
public interface IRoleCommandRepository extends JpaRepository<Role, Integer> {

    /**
     * Busca un rol por nombre.
     */
    Optional<Role> findByName(String name);
}

