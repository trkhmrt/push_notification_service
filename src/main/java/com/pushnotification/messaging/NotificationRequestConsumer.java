package com.pushnotification.messaging;

import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.service.NotificationPublisher;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRequestConsumer {

    private final NotificationPublisher notificationPublisher;
    private final Validator validator;

    @RabbitListener(queues = "${app.rabbitmq.inbound.queue}")
    public void consume(NotificationRequest request) {
        Set<ConstraintViolation<NotificationRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            String details = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            log.warn("Invalid notification request from queue. details={}", details);
            return;
        }

        notificationPublisher.publish(request, null);
        if (log.isInfoEnabled()) {
            log.info(
                    "Notification request consumed from queue. serviceName={}, channels={}",
                    request.serviceName(),
                    request.channels()
            );
        }
    }
}
