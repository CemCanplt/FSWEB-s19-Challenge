package com.twitter.mavikus.dto.auth;

import java.time.Instant;

/**
 * Register işlemi sonrası kullanıcıya döndürülecek güvenli bilgileri içeren DTO
 */
public record RegisterResponseDTO(
        Long id,
        String userName,
        String email,
        Instant createdAt,
        boolean success,
        String message
) {
}