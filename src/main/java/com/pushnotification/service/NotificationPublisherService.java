package com.pushnotification.service;

import com.pushnotification.config.RabbitMQProperties;
import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.dto.ChannelResult;
import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.NotificationResponse;
import com.pushnotification.messaging.ChannelMessageMapper;
import com.pushnotification.messaging.ChannelMessageMapperRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationPublisherService implements NotificationPublisher {

    private static final String QUEUED_STATUS = "QUEUED";

    private final RabbitTemplate rabbitTemplate;
    private final RabbitMQProperties rabbitMQProperties;
    private final ChannelMessageMapperRegistry channelMessageMapperRegistry;
    private final NotificationRecordService notificationRecordService;

    @Override
    public NotificationResponse publish(NotificationRequest request, ClientMetadata clientMetadata) {
        UUID eventId = request.eventId() == null ? UUID.randomUUID() : request.eventId();
        notificationRecordService.save(eventId, request, clientMetadata);
        List<ChannelResult> results = new ArrayList<>();

        for (NotificationChannel channel : request.channels()) {
            ChannelMessageMapper mapper = channelMessageMapperRegistry.get(channel);
            String routingKey = resolveRoutingKey(channel);
            String queueName = resolveQueueName(channel);
            Object payload = mapper.map(request);

            rabbitTemplate.convertAndSend(
                    rabbitMQProperties.exchange(),
                    routingKey,
                    payload
            );

            if (log.isInfoEnabled()) {
                log.info(
                        "Notification queued. eventId={}, serviceName={}, channel={}, routingKey={}",
                        eventId,
                        request.serviceName(),
                        channel,
                        routingKey
                );
            }

            results.add(new ChannelResult(channel, routingKey, queueName, QUEUED_STATUS));
        }

        return new NotificationResponse(eventId.toString(), List.copyOf(results));
    }

    private String resolveRoutingKey(NotificationChannel channel) {
        return switch (channel) {
            case MAIL -> rabbitMQProperties.routingKeys().mail();
            case TELEGRAM -> rabbitMQProperties.routingKeys().telegram();
        };
    }

    private String resolveQueueName(NotificationChannel channel) {
        return switch (channel) {
            case MAIL -> rabbitMQProperties.queues().mail();
            case TELEGRAM -> rabbitMQProperties.queues().telegram();
        };
    }
}
