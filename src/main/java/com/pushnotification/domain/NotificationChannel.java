package com.pushnotification.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationChannel {

    MAIL("mail"),
    TELEGRAM("telegram");

    private final String value;

    NotificationChannel(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static NotificationChannel fromValue(String value) {
        for (NotificationChannel channel : values()) {
            if (channel.value.equalsIgnoreCase(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unsupported notification channel: " + value);
    }
}
