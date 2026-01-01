package com.challenge.ecommercebackend.config.redis;

import com.challenge.ecommercebackend.modules.user.persisten.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Servicio para manejar tokens JWT en Redis
 * Reemplaza el almacenamiento de tokens en PostgreSQL
 */
@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTokenRepository redisTokenRepository;

    @Value("${expiration.time.token:1}")
    private Integer expirationTimeDays;

    /**
     * Guarda un nuevo token para un usuario
     */
    public RedisToken saveToken(User user, String jwtToken) {
        long now = System.currentTimeMillis();
        long expiresAt = now + (expirationTimeDays * 24 * 60 * 60 * 1000L);

        RedisToken redisToken = RedisToken.builder()
                .id(UUID.randomUUID().toString())
                .token(jwtToken)
                .userId(user.getId())
                .userEmail(user.getEmail())
                .tokenType("BEARER")
                .revoked(false)
                .expired(false)
                .createdAt(now)
                .expiresAt(expiresAt)
                .build();

        return redisTokenRepository.save(redisToken);
    }

    /**
     * Busca un token por su valor
     */
    public Optional<RedisToken> findByToken(String token) {
        return redisTokenRepository.findByToken(token);
    }

    /**
     * Verifica si un token es válido
     */
    public boolean isTokenValid(String token) {
        return redisTokenRepository.findByToken(token)
                .map(t -> !t.isExpired() && !t.isRevoked() && t.getExpiresAt() > System.currentTimeMillis())
                .orElse(false);
    }

    /**
     * Revoca todos los tokens de un usuario
     */
    public void revokeAllUserTokens(Long userId) {
        List<RedisToken> validTokens = redisTokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(userId);
        validTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
            redisTokenRepository.save(token);
        });
    }

    /**
     * Revoca un token específico
     */
    public void revokeToken(String token) {
        redisTokenRepository.findByToken(token).ifPresent(t -> {
            t.setExpired(true);
            t.setRevoked(true);
            redisTokenRepository.save(t);
        });
    }

    /**
     * Elimina todos los tokens de un usuario
     */
    public void deleteAllUserTokens(Long userId) {
        redisTokenRepository.deleteByUserId(userId);
    }

    /**
     * Obtiene todos los tokens válidos de un usuario
     */
    public List<RedisToken> findAllValidTokensByUser(Long userId) {
        return redisTokenRepository.findByUserIdAndExpiredFalseAndRevokedFalse(userId);
    }

    /**
     * Elimina tokens expirados (limpieza)
     */
    public void cleanupExpiredTokens() {
        // Redis maneja esto automáticamente con TTL
        // Este método es para limpieza manual si es necesario
    }
}

