package com.pushnotification.messaging;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.dto.NotificationRequest;

public interface ChannelMessageMapper {

    NotificationChannel channel();

    Object map(NotificationRequest request);
}
