package dev.murilodcosta.auth_service.dto;

import dev.murilodcosta.auth_service.domain.entities.User;
import dev.murilodcosta.auth_service.domain.enums.Role;

import java.util.UUID;

public record UserResponseDto(
        UUID id,
        String username,
        String email,
        Role role
) {
    public static UserResponseDto fromEntity(User user) {
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
}
