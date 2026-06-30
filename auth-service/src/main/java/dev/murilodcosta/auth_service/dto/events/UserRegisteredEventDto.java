package dev.murilodcosta.auth_service.dto.events;

import java.io.Serializable;

public record UserRegisteredEventDto(
        String name,
        String email
) implements Serializable {
}
