package com.pushnotification.controller;

import com.pushnotification.dto.NotificationRecordResponse;
import com.pushnotification.service.NotificationRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification Records", description = "Notification audit and read status operations")
public class NotificationRecordController {

    private final NotificationRecordService notificationRecordService;

    @GetMapping("/{eventId}")
    @Operation(summary = "Get notification record by event id")
    @ApiResponse(responseCode = "200", description = "Notification record returned")
    public NotificationRecordResponse getByEventId(@PathVariable UUID eventId) {
        return notificationRecordService.findByEventId(eventId);
    }

    @PatchMapping("/{eventId}/read")
    @Operation(summary = "Mark notification as read")
    @ApiResponse(responseCode = "200", description = "Notification marked as read")
    public NotificationRecordResponse markAsRead(@PathVariable UUID eventId) {
        return notificationRecordService.markAsRead(eventId);
    }
}
