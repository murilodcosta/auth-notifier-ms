package dev.murilodcosta.notification_service.consumer;

import dev.murilodcosta.notification_service.dto.UserRegisteredEventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationConsumer {

    @RabbitListener(queues = "user.registration.notifications")
    public void handleUserRegistration(UserRegisteredEventDto event) {
        log.info("-----------------------------------------------------------------");
        log.info("Processing User Registration Event in Background Worker...");
        log.info("Successfully sent welcome e-mail to: {} (Name: {})", event.email(), event.name());
        log.info("-----------------------------------------------------------------");
    }
}
