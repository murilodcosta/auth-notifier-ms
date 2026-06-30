package dev.murilodcosta.auth_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
public record LoginRequestDto(
    @NotBlank String email,
    @NotBlank String password
){
}
