package com.pushnotification.service;

import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.NotificationResponse;

public interface NotificationPublisher {

    NotificationResponse publish(NotificationRequest request, ClientMetadata clientMetadata);
}
