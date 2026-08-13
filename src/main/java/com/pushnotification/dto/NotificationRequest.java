package com.pushnotification.dto;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.validation.ValidNotificationRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ValidNotificationRequest
public record NotificationRequest(
        UUID eventId,
        @NotEmpty List<NotificationChannel> channels,
        @NotBlank @Size(max = 100) String serviceName,
        @NotNull NotificationMessageType messageType,
        Recipients recipients,
        @Size(max = 200) String subject,
        Map<String, Object> templateData,
        Boolean kvkkApproved,
        ContactInfo contact,
        ClientMetadata client
) {

    public NotificationRequest {
        channels = channels == null ? List.of() : List.copyOf(channels);
        templateData = templateData == null ? Collections.emptyMap() : Map.copyOf(templateData);
    }
}
