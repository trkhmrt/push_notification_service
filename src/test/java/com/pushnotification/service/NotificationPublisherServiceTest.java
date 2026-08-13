package com.pushnotification.service;

import com.pushnotification.config.RabbitMQProperties;
import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.NotificationResponse;
import com.pushnotification.dto.Recipients;
import com.pushnotification.messaging.ChannelMessageMapperRegistry;
import com.pushnotification.messaging.mail.MailMessageMapper;
import com.pushnotification.messaging.mail.MailQueueMessage;
import com.pushnotification.messaging.telegram.TelegramMessageMapper;
import com.pushnotification.messaging.telegram.TelegramQueueMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationPublisherServiceTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private RabbitMQProperties rabbitMQProperties;

    @Mock
    private ChannelMessageMapperRegistry channelMessageMapperRegistry;

    @Mock
    private NotificationRecordService notificationRecordService;

    @InjectMocks
    private NotificationPublisherService notificationPublisherService;

    @BeforeEach
    void setUp() {
        RabbitMQProperties.Queues queues = new RabbitMQProperties.Queues(
                "mail.send.queue",
                "telegram.send.queue"
        );
        RabbitMQProperties.RoutingKeys routingKeys = new RabbitMQProperties.RoutingKeys(
                "mail.send",
                "telegram.send"
        );

        when(rabbitMQProperties.exchange()).thenReturn("notification.topic");
        when(rabbitMQProperties.routingKeys()).thenReturn(routingKeys);
        when(rabbitMQProperties.queues()).thenReturn(queues);
        when(channelMessageMapperRegistry.get(NotificationChannel.MAIL)).thenReturn(new MailMessageMapper());
        lenient().when(channelMessageMapperRegistry.get(NotificationChannel.TELEGRAM))
                .thenReturn(new TelegramMessageMapper());
    }

    @Test
    void publish_whenMailChannelSelected_thenSendMailQueueMessage() {
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL),
                "payment-service",
                NotificationMessageType.PAYMENT_CONFIRMATION,
                new Recipients("user@example.com", List.of(), List.of()),
                "Payment received",
                Map.of("customerName", "Tarik"),
                null,
                null,
                null
        );

        NotificationResponse response = notificationPublisherService.publish(request, null);

        ArgumentCaptor<MailQueueMessage> messageCaptor = ArgumentCaptor.forClass(MailQueueMessage.class);
        verify(rabbitTemplate).convertAndSend(
                eq("notification.topic"),
                eq("mail.send"),
                messageCaptor.capture()
        );

        MailQueueMessage message = messageCaptor.getValue();
        assertThat(message.serviceName()).isEqualTo("payment-service");
        assertThat(message.to()).isEqualTo("user@example.com");
        assertThat(response.results()).hasSize(1);
        assertThat(response.results().getFirst().channel()).isEqualTo(NotificationChannel.MAIL);
        assertThat(response.results().getFirst().routingKey()).isEqualTo("mail.send");
    }

    @Test
    void publish_whenTrialExpiryReminder_thenMapMailMessageTypeAndTemplateData() {
        Map<String, Object> templateData = Map.of(
                "userName", "Tarik",
                "packageName", "Dijital Menü PRO",
                "expiresAt", "2026-07-19T09:00:00",
                "daysRemaining", 3,
                "upgradeUrl", "https://example.com/packages"
        );
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL),
                "qr-service",
                NotificationMessageType.PRO_TRIAL_EXPIRY_REMINDER,
                new Recipients("user@example.com", List.of(), List.of()),
                null,
                templateData,
                null,
                null,
                null
        );

        notificationPublisherService.publish(request, null);

        ArgumentCaptor<MailQueueMessage> messageCaptor = ArgumentCaptor.forClass(MailQueueMessage.class);
        verify(rabbitTemplate).convertAndSend(
                eq("notification.topic"),
                eq("mail.send"),
                messageCaptor.capture()
        );
        MailQueueMessage message = messageCaptor.getValue();
        assertThat(message.messageType().name()).isEqualTo("PRO_TRIAL_EXPIRY_REMINDER");
        assertThat(message.templateData()).isEqualTo(templateData);
    }

    @Test
    void publish_whenBothChannelsSelected_thenPublishTwice() {
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL, NotificationChannel.TELEGRAM),
                "payment-service",
                NotificationMessageType.REQUEST_FORM,
                new Recipients("user@example.com", List.of(), List.of()),
                "Contact form",
                Map.of("firstName", "Tarik", "lastName", "Yilmaz", "phone", "+90555", "message", "Hello"),
                true,
                null,
                null
        );

        NotificationResponse response = notificationPublisherService.publish(request, null);

        verify(rabbitTemplate).convertAndSend(eq("notification.topic"), eq("mail.send"), org.mockito.ArgumentMatchers.any(MailQueueMessage.class));
        verify(rabbitTemplate).convertAndSend(eq("notification.topic"), eq("telegram.send"), org.mockito.ArgumentMatchers.any(TelegramQueueMessage.class));
        assertThat(response.results()).hasSize(2);
    }
}
