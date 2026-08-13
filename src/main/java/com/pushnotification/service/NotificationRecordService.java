package com.pushnotification.service;

import com.pushnotification.domain.NotificationRecord;
import com.pushnotification.dto.ClientMetadata;
import com.pushnotification.dto.NotificationRecordResponse;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.exception.NotificationRecordNotFoundException;
import com.pushnotification.mapper.NotificationRecordMapper;
import com.pushnotification.repository.NotificationRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationRecordService {

    private final NotificationRecordRepository notificationRecordRepository;
    private final NotificationRecordFactory notificationRecordFactory;
    private final NotificationRecordMapper notificationRecordMapper;

    @Transactional
    public void save(UUID eventId, NotificationRequest request, ClientMetadata clientMetadata) {
        NotificationRecord record = notificationRecordFactory.create(eventId, request, clientMetadata);
        notificationRecordRepository.save(record);
    }

    @Transactional(readOnly = true)
    public NotificationRecordResponse findByEventId(UUID eventId) {
        return notificationRecordRepository.findById(eventId)
                .map(notificationRecordMapper::toResponse)
                .orElseThrow(() -> new NotificationRecordNotFoundException(eventId));
    }

    @Transactional
    public NotificationRecordResponse markAsRead(UUID eventId) {
        NotificationRecord record = notificationRecordRepository.findById(eventId)
                .orElseThrow(() -> new NotificationRecordNotFoundException(eventId));

        if (!record.isRead()) {
            record.setRead(true);
            record.setReadAt(OffsetDateTime.now());
        }

        return notificationRecordMapper.toResponse(record);
    }
}
