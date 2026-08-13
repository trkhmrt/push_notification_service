package com.pushnotification.exception;

import java.util.UUID;

public class NotificationRecordNotFoundException extends RuntimeException {

    public NotificationRecordNotFoundException(UUID eventId) {
        super("Notification record not found for eventId: " + eventId);
    }
}
