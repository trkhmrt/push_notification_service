package com.pushnotification.service;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.ContactInfo;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.Recipients;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationRecordFactoryTest {

    private final NotificationRecordFactory notificationRecordFactory = new NotificationRecordFactory();

    @Test
    void create_whenKvkkApproved_thenPersistConsentAndContactFields() {
        UUID eventId = UUID.randomUUID();
        NotificationRequest request = new NotificationRequest(
                eventId,
                List.of(NotificationChannel.MAIL, NotificationChannel.TELEGRAM),
                "algory-site",
                NotificationMessageType.REQUEST_FORM,
                new Recipients("user@example.com", List.of(), List.of()),
                "Education application",
                Map.of("message", "Java 101"),
                true,
                new ContactInfo("Tarik", "Yilmaz", "user@example.com", "+905551112233"),
                new ClientMetadata("127.0.0.1", "Mozilla/5.0", "Windows 11", "desktop")
        );

        var record = notificationRecordFactory.create(
                eventId,
                request,
                new ClientMetadata("127.0.0.1", "Mozilla/5.0", "Windows 11", "desktop")
        );

        assertThat(record.getId()).isEqualTo(eventId);
        assertThat(record.isKvkkApproved()).isTrue();
        assertThat(record.getKvkkApprovedAt()).isNotNull();
        assertThat(record.isRead()).isFalse();
        assertThat(record.getFirstName()).isEqualTo("Tarik");
        assertThat(record.getLastName()).isEqualTo("Yilmaz");
        assertThat(record.getEmail()).isEqualTo("user@example.com");
        assertThat(record.getPhone()).isEqualTo("+905551112233");
        assertThat(record.getIpAddress()).isEqualTo("127.0.0.1");
        assertThat(record.getUserAgent()).contains("Mozilla");
        assertThat(record.getChannels()).isEqualTo("MAIL,TELEGRAM");
    }
}
