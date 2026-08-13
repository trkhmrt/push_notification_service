package com.pushnotification.service;

import com.pushnotification.domain.NotificationRecord;
import com.pushnotification.dto.NotificationRecordResponse;
import com.pushnotification.exception.NotificationRecordNotFoundException;
import com.pushnotification.mapper.NotificationRecordMapper;
import com.pushnotification.repository.NotificationRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NotificationRecordServiceTest {

    @Mock
    private NotificationRecordRepository notificationRecordRepository;

    @Mock
    private NotificationRecordFactory notificationRecordFactory;

    @Mock
    private NotificationRecordMapper notificationRecordMapper;

    @InjectMocks
    private NotificationRecordService notificationRecordService;

    @Test
    void markAsRead_whenRecordExists_thenUpdateReadState() {
        UUID eventId = UUID.randomUUID();
        NotificationRecord record = new NotificationRecord();
        record.setId(eventId);
        record.setRead(false);

        NotificationRecordResponse response = new NotificationRecordResponse(
                eventId.toString(),
                "algory-site",
                "REQUEST_FORM",
                "TELEGRAM",
                "user@example.com",
                "Tarik",
                "Yilmaz",
                "+90555",
                null,
                true,
                OffsetDateTime.now(),
                true,
                OffsetDateTime.now(),
                "127.0.0.1",
                "Mozilla/5.0",
                "Windows",
                "desktop",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );

        given(notificationRecordRepository.findById(eventId)).willReturn(Optional.of(record));
        given(notificationRecordMapper.toResponse(record)).willReturn(response);

        NotificationRecordResponse result = notificationRecordService.markAsRead(eventId);

        assertThat(result.read()).isTrue();
        assertThat(record.isRead()).isTrue();
        assertThat(record.getReadAt()).isNotNull();
        verify(notificationRecordRepository).findById(eventId);
    }

    @Test
    void findByEventId_whenRecordMissing_thenThrowException() {
        UUID eventId = UUID.randomUUID();
        given(notificationRecordRepository.findById(eventId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> notificationRecordService.findByEventId(eventId))
                .isInstanceOf(NotificationRecordNotFoundException.class);
    }
}
