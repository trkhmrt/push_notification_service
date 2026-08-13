package com.pushnotification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.rabbitmq")
public record RabbitMQProperties(
        String exchange,
        Inbound inbound,
        Queues queues,
        RoutingKeys routingKeys
) {

    public record Inbound(
            String exchange,
            String queue,
            String routingKey
    ) {
    }

    public record Queues(
            String mail,
            String telegram
    ) {
    }

    public record RoutingKeys(
            String mail,
            String telegram
    ) {
    }
}
