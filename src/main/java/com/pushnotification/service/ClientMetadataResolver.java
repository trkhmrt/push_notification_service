package com.pushnotification.service;

import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.NotificationRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class ClientMetadataResolver {

    public ClientMetadata resolve(NotificationRequest request, HttpServletRequest httpRequest) {
        ClientMetadata requestMetadata = request.client();
        return new ClientMetadata(
                firstNonBlank(
                        requestMetadata == null ? null : requestMetadata.ipAddress(),
                        extractIpAddress(httpRequest)
                ),
                firstNonBlank(
                        requestMetadata == null ? null : requestMetadata.userAgent(),
                        httpRequest == null ? null : httpRequest.getHeader("User-Agent")
                ),
                requestMetadata == null ? null : requestMetadata.deviceInfo(),
                requestMetadata == null ? null : requestMetadata.deviceType()
        );
    }

    private String extractIpAddress(HttpServletRequest httpRequest) {
        if (httpRequest == null) {
            return null;
        }

        String forwardedFor = httpRequest.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwardedFor)) {
            return forwardedFor.split(",")[0].trim();
        }

        String realIp = httpRequest.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }

        return httpRequest.getRemoteAddr();
    }

    private String firstNonBlank(String... values) {
        for (String value : values) {
            if (StringUtils.hasText(value)) {
                return value.trim();
            }
        }
        return null;
    }
}
