package com.pushnotification.messaging.telegram;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Collections;
import java.util.Map;

public record TelegramQueueMessage(
        @NotBlank String serviceName,
        @NotNull TelegramMessageType messageType,
        Map<String, Object> templateData
) {

    public TelegramQueueMessage {
        templateData = templateData == null ? Collections.emptyMap() : Map.copyOf(templateData);
    }
}
