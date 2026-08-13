package com.pushnotification.controller;

import com.pushnotification.dto.NotificationRecordResponse;
import com.pushnotification.service.NotificationRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationRecordController.class)
class NotificationRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationRecordService notificationRecordService;

    @Test
    void getByEventId_whenRecordExists_thenReturn200() throws Exception {
        UUID eventId = UUID.randomUUID();
        given(notificationRecordService.findByEventId(eventId)).willReturn(sampleResponse(eventId));

        mockMvc.perform(get("/api/v1/notifications/{eventId}", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value(eventId.toString()))
                .andExpect(jsonPath("$.kvkkApproved").value(true))
                .andExpect(jsonPath("$.read").value(false));
    }

    @Test
    void markAsRead_whenRecordExists_thenReturn200() throws Exception {
        UUID eventId = UUID.randomUUID();
        given(notificationRecordService.markAsRead(eventId)).willReturn(readResponse(eventId));

        mockMvc.perform(patch("/api/v1/notifications/{eventId}/read", eventId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read").value(true));
    }

    private NotificationRecordResponse readResponse(UUID eventId) {
        return new NotificationRecordResponse(
                eventId.toString(),
                "algory-site",
                "REQUEST_FORM",
                "TELEGRAM",
                "user@example.com",
                "Tarik",
                "Yilmaz",
                "+90555",
                "Education application",
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
    }

    private NotificationRecordResponse sampleResponse(UUID eventId) {
        return new NotificationRecordResponse(
                eventId.toString(),
                "algory-site",
                "REQUEST_FORM",
                "TELEGRAM",
                "user@example.com",
                "Tarik",
                "Yilmaz",
                "+90555",
                "Education application",
                true,
                OffsetDateTime.now(),
                false,
                null,
                "127.0.0.1",
                "Mozilla/5.0",
                "Windows",
                "desktop",
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }
}
