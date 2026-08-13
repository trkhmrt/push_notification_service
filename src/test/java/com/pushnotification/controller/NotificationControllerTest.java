package com.pushnotification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.domain.NotificationMessageType;
import com.pushnotification.dto.ChannelResult;
import com.pushnotification.dto.ContactInfo;
import com.pushnotification.dto.NotificationRequest;
import com.pushnotification.dto.NotificationResponse;
import com.pushnotification.dto.Recipients;
import com.pushnotification.service.ClientMetadataResolver;
import com.pushnotification.service.NotificationPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationPublisher notificationPublisher;

    @MockitoBean
    private ClientMetadataResolver clientMetadataResolver;

    @Test
    void publish_whenRequestIsValid_thenReturn202() throws Exception {
        NotificationRequest request = new NotificationRequest(
                null,
                List.of(NotificationChannel.MAIL),
                "payment-service",
                NotificationMessageType.GENERIC,
                new Recipients("user@example.com", List.of(), List.of()),
                "Subject",
                Map.of("message", "Body"),
                true,
                new ContactInfo("Tarik", "Yilmaz", "user@example.com", "+90555"),
                null
        );

        given(notificationPublisher.publish(any(NotificationRequest.class), any()))
                .willReturn(new NotificationResponse(
                        "message-id",
                        List.of(new ChannelResult(NotificationChannel.MAIL, "mail.send", "mail.send.queue", "QUEUED"))
                ));

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.eventId").value("message-id"))
                .andExpect(jsonPath("$.results[0].status").value("QUEUED"))
                .andExpect(jsonPath("$.results[0].routingKey").value("mail.send"));
    }

    @Test
    void publish_whenChannelsMissing_thenReturn400() throws Exception {
        String invalidRequest = """
                {
                  "serviceName": "payment-service",
                  "messageType": "GENERIC",
                  "recipients": { "email": "user@example.com" },
                  "templateData": { "message": "Body" }
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void publish_whenMailSelectedWithoutEmail_thenReturn400() throws Exception {
        String invalidRequest = """
                {
                  "channels": ["mail"],
                  "serviceName": "payment-service",
                  "messageType": "GENERIC",
                  "templateData": { "message": "Body" }
                }
                """;

        mockMvc.perform(post("/api/v1/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
