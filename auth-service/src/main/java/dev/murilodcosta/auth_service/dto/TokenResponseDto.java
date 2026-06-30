package dev.murilodcosta.auth_service.dto;

import lombok.Builder;

@Builder
public record TokenResponseDto(
        String token,
        long expiresIn
) {
}
