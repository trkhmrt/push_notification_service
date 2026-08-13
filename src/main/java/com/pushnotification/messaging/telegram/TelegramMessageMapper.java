package com.pushnotification.messaging.telegram;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.messaging.ChannelMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class TelegramMessageMapper implements ChannelMessageMapper {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.TELEGRAM;
    }

    @Override
    public Object map(NotificationRequest request) {
        return new TelegramQueueMessage(
                request.serviceName(),
                mapMessageType(request.messageType()),
                request.templateData()
        );
    }

    private TelegramMessageType mapMessageType(NotificationMessageType messageType) {
        return TelegramMessageType.valueOf(messageType.name());
    }
}
