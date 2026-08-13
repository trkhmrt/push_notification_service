package com.pushnotification.dto;

import java.util.List;

public record NotificationResponse(
        String eventId,
        List<ChannelResult> results
) {
}
