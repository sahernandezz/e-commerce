package com.challenge.ecommercebackend.config.redis;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para manejar tokens en Redis
 */
@Repository
public interface RedisTokenRepository extends CrudRepository<RedisToken, String> {

    /**
     * Busca un token por su valor
     */
    Optional<RedisToken> findByToken(String token);

    /**
     * Busca todos los tokens de un usuario por su ID
     */
    List<RedisToken> findByUserId(Long userId);

    /**
     * Busca todos los tokens de un usuario por su email
     */
    List<RedisToken> findByUserEmail(String userEmail);

    /**
     * Busca todos los tokens válidos de un usuario
     */
    List<RedisToken> findByUserIdAndExpiredFalseAndRevokedFalse(Long userId);

    /**
     * Elimina todos los tokens de un usuario
     */
    void deleteByUserId(Long userId);

    /**
     * Elimina un token específico
     */
    void deleteByToken(String token);
}

