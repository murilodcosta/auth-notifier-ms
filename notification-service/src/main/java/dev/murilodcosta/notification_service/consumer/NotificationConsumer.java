package dev.murilodcosta.notification_service.consumer;

import dev.murilodcosta.notification_service.dto.UserRegisteredEventDto;
import dev.murilodcosta.notification_service.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "user.registration.notifications")
    public void handleUserRegistration(UserRegisteredEventDto event) {
        log.info("-----------------------------------------------------------------");
        log.info("Processing User Registration Event in Background Worker...");
        emailService.sendWelcomeEmail(event.email(), event.name());
        log.info("-----------------------------------------------------------------");
    }
}
