package com.pushnotification.messaging.mail;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.messaging.ChannelMessageMapper;
import org.springframework.stereotype.Component;

@Component
public class MailMessageMapper implements ChannelMessageMapper {

    @Override
    public NotificationChannel channel() {
        return NotificationChannel.MAIL;
    }

    @Override
    public Object map(NotificationRequest request) {
        return new MailQueueMessage(
                request.serviceName(),
                mapMessageType(request.messageType()),
                request.recipients().email(),
                request.recipients().cc(),
                request.recipients().bcc(),
                request.subject(),
                stringValue(request.templateData().get("message")),
                false,
                request.templateData()
        );
    }

    private MailMessageType mapMessageType(NotificationMessageType messageType) {
        return MailMessageType.valueOf(messageType.name());
    }

    private String stringValue(Object value) {
        return value == null ? null : value.toString();
    }
}
