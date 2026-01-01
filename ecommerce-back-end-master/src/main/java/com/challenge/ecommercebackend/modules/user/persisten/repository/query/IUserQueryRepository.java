package com.challenge.ecommercebackend.modules.user.persisten.repository.query;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio de LECTURA (Query) para usuarios.
 * Usa la vista materializada auth.customer_view que ya filtra por status ACTIVE.
 */
@Repository
public interface IUserQueryRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por email.
     */
    @Query(value = "SELECT * FROM auth.customer_view WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * Busca un usuario por ID.
     */
    @Query(value = "SELECT * FROM auth.customer_view WHERE id = :id", nativeQuery = true)
    Optional<User> findActiveById(@Param("id") Long id);
}

