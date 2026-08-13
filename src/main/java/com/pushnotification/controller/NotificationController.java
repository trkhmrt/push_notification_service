package com.pushnotification.controller;

import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.NotificationResponse;
import com.pushnotification.service.ClientMetadataResolver;
import com.pushnotification.service.NotificationPublisher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "Notification publish operations")
public class NotificationController {

    private final NotificationPublisher notificationPublisher;
    private final ClientMetadataResolver clientMetadataResolver;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Publish notification to RabbitMQ topic exchange")
    @ApiResponse(responseCode = "202", description = "Notification accepted and queued")
    public NotificationResponse publish(
            @Valid @RequestBody NotificationRequest request,
            HttpServletRequest httpServletRequest
    ) {
        return notificationPublisher.publish(
                request,
                clientMetadataResolver.resolve(request, httpServletRequest)
        );
    }
}
