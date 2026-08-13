package com.pushnotification.dto;

import java.time.OffsetDateTime;

public record NotificationRecordResponse(
        String eventId,
        String serviceName,
        String messageType,
        String channels,
        String email,
        String firstName,
        String lastName,
        String phone,
        String subject,
        boolean kvkkApproved,
        OffsetDateTime kvkkApprovedAt,
        boolean read,
        OffsetDateTime readAt,
        String ipAddress,
        String userAgent,
        String deviceInfo,
        String deviceType,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
