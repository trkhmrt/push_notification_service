package com.pushnotification.mapper;

import com.pushnotification.domain.NotificationRecord;
import com.pushnotification.dto.NotificationRecordResponse;
import org.springframework.stereotype.Component;

@Component
public class NotificationRecordMapper {

    public NotificationRecordResponse toResponse(NotificationRecord record) {
        return new NotificationRecordResponse(
                record.getId().toString(),
                record.getServiceName(),
                record.getMessageType(),
                record.getChannels(),
                record.getEmail(),
                record.getFirstName(),
                record.getLastName(),
                record.getPhone(),
                record.getSubject(),
                record.isKvkkApproved(),
                record.getKvkkApprovedAt(),
                record.isRead(),
                record.getReadAt(),
                record.getIpAddress(),
                record.getUserAgent(),
                record.getDeviceInfo(),
                record.getDeviceType(),
                record.getCreatedAt(),
                record.getUpdatedAt()
        );
    }
}
