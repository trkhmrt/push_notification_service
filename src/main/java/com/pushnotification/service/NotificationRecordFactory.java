package com.pushnotification.service;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationRecord;
import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.ContactInfo;
import com.pushnotification.dto.NotificationRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class NotificationRecordFactory {

    public NotificationRecord create(UUID eventId, NotificationRequest request, ClientMetadata clientMetadata) {
        ContactInfo contactInfo = resolveContactInfo(request);
        NotificationRecord record = new NotificationRecord();
        record.setId(eventId);
        record.setServiceName(request.serviceName());
        record.setMessageType(request.messageType().name());
        record.setChannels(formatChannels(request.channels()));
        record.setEmail(contactInfo.email());
        record.setFirstName(contactInfo.firstName());
        record.setLastName(contactInfo.lastName());
        record.setPhone(contactInfo.phone());
        record.setSubject(request.subject());
        record.setTemplateData(request.templateData());
        record.setKvkkApproved(Boolean.TRUE.equals(request.kvkkApproved()));
        if (record.isKvkkApproved()) {
            record.setKvkkApprovedAt(OffsetDateTime.now());
        }
        record.setRead(false);
        applyClientMetadata(record, clientMetadata);
        return record;
    }

    private ContactInfo resolveContactInfo(NotificationRequest request) {
        ContactInfo contact = request.contact();
        String email = firstNonBlank(
                contact == null ? null : contact.email(),
                request.recipients() == null ? null : request.recipients().email(),
                readTemplateValue(request.templateData(), "email")
        );
        String firstName = firstNonBlank(
                contact == null ? null : contact.firstName(),
                readTemplateValue(request.templateData(), "firstName")
        );
        String lastName = firstNonBlank(
                contact == null ? null : contact.lastName(),
                readTemplateValue(request.templateData(), "lastName")
        );
        String phone = firstNonBlank(
                contact == null ? null : contact.phone(),
                readTemplateValue(request.templateData(), "phone")
        );
        return new ContactInfo(firstName, lastName, email, phone);
    }

    private void applyClientMetadata(NotificationRecord record, ClientMetadata clientMetadata) {
        if (clientMetadata == null) {
            return;
        }
        record.setIpAddress(clientMetadata.ipAddress());
        record.setUserAgent(clientMetadata.userAgent());
        record.setDeviceInfo(clientMetadata.deviceInfo());
        record.setDeviceType(clientMetadata.deviceType());
    }

    private String formatChannels(java.util.List<NotificationChannel> channels) {
        return channels.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    private String readTemplateValue(Map<String, Object> templateData, String key) {
        if (templateData == null || !templateData.containsKey(key)) {
            return null;
        }
        Object value = templateData.get(key);
        return value == null ? null : String.valueOf(value);
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
