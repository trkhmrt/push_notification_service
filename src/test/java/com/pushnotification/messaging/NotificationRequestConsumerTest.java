package com.pushnotification.messaging;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.Recipients;
import com.pushnotification.service.NotificationPublisher;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class NotificationRequestConsumerTest {

    @Mock
    private NotificationPublisher notificationPublisher;

    @InjectMocks
    private NotificationRequestConsumer notificationRequestConsumer;

    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        notificationRequestConsumer = new NotificationRequestConsumer(notificationPublisher, validator);
    }

    @Test
    void consume_whenValidRequest_thenPublish() {
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL),
                "payment-service",
                NotificationMessageType.PAYMENT_CONFIRMATION,
                new Recipients("user@example.com", List.of(), List.of()),
                "Payment received",
                Map.of("customerName", "Tarik"),
                true,
                null,
                null
        );

        notificationRequestConsumer.consume(request);

        verify(notificationPublisher).publish(eq(request), isNull());
    }

    @Test
    void consume_whenMailChannelWithoutEmail_thenSkipPublish() {
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL),
                "payment-service",
                NotificationMessageType.PAYMENT_CONFIRMATION,
                null,
                "Payment received",
                Map.of(),
                true,
                null,
                null
        );

        notificationRequestConsumer.consume(request);

        verifyNoInteractions(notificationPublisher);
    }
}
