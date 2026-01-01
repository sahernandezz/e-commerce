package com.challenge.ecommercebackend.config.redis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;

/**
 * Entidad para almacenar tokens JWT en Redis
 * Reemplaza la tabla auth.token de PostgreSQL
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "token", timeToLive = 86400) // TTL de 24 horas por defecto
public class RedisToken implements Serializable {

    @Id
    private String id;

    @Indexed
    private String token;

    @Indexed
    private Long userId;

    @Indexed
    private String userEmail;

    private String tokenType;

    private boolean revoked;

    private boolean expired;

    private long createdAt;

    private long expiresAt;
}

