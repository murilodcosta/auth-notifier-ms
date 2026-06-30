package dev.murilodcosta.notification_service.dto;

import java.io.Serializable;

public record UserRegisteredEventDto(
        String name,
        String email
) implements Serializable {
}
