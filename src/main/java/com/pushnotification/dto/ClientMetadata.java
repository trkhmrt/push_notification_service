package com.pushnotification.dto;

import jakarta.validation.constraints.Size;

public record ClientMetadata(
        @Size(max = 45) String ipAddress,
        @Size(max = 512) String userAgent,
        @Size(max = 255) String deviceInfo,
        @Size(max = 50) String deviceType
) {
}
