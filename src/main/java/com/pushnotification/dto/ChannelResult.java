package com.pushnotification.dto;

import com.pushnotification.domain.NotificationChannel;

public record ChannelResult(
        NotificationChannel channel,
        String routingKey,
        String queue,
        String status
) {
}
