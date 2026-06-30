package dev.murilodcosta.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record RegisterRequestDto (
    @NotBlank String username,
    @NotBlank String email,
    @NotBlank String password
){
}
