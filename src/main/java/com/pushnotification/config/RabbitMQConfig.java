package com.pushnotification.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RabbitMQProperties.class)
public class RabbitMQConfig {

    @Bean
    TopicExchange notificationExchange(RabbitMQProperties properties) {
        return new TopicExchange(properties.exchange(), true, false);
    }

    @Bean
    TopicExchange pushNotificationInboundExchange(RabbitMQProperties properties) {
        return new TopicExchange(properties.inbound().exchange(), true, false);
    }

    @Bean
    Queue pushNotificationInboundQueue(RabbitMQProperties properties) {
        return QueueBuilder.durable(properties.inbound().queue()).build();
    }

    @Bean
    Binding pushNotificationInboundBinding(
            Queue pushNotificationInboundQueue,
            TopicExchange pushNotificationInboundExchange,
            RabbitMQProperties properties
    ) {
        return BindingBuilder
                .bind(pushNotificationInboundQueue)
                .to(pushNotificationInboundExchange)
                .with(properties.inbound().routingKey());
    }

    @Bean
    Binding mailBinding(TopicExchange notificationExchange, RabbitMQProperties properties) {
        Queue mailQueue = new Queue(properties.queues().mail());
        mailQueue.setShouldDeclare(false);
        return BindingBuilder
                .bind(mailQueue)
                .to(notificationExchange)
                .with(properties.routingKeys().mail());
    }

    @Bean
    Binding telegramBinding(TopicExchange notificationExchange, RabbitMQProperties properties) {
        Queue telegramQueue = new Queue(properties.queues().telegram());
        telegramQueue.setShouldDeclare(false);
        return BindingBuilder
                .bind(telegramQueue)
                .to(notificationExchange)
                .with(properties.routingKeys().telegram());
    }

    @Bean
    MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
