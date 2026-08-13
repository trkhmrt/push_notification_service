package com.pushnotification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationMessageType {

    PASSWORD_RESET,
    NEW_REGISTRATION,
    PAYMENT_CONFIRMATION,
    PAYMENT_REFUND,
    PRO_TRIAL_EXPIRY_REMINDER,
    REQUEST_FORM,
    GENERIC;

    @JsonCreator
    public static NotificationMessageType fromValue(String value) {
        return NotificationMessageType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name();
    }
}
