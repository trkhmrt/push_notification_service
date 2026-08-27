package com.pushnotification.messaging.mail;

import com.pushnotification.dto.SmtpAccount;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public record MailQueueMessage(
        String serviceName,
        MailMessageType messageType,
        String to,
        List<String> cc,
        List<String> bcc,
        String subject,
        String body,
        Boolean html,
        Map<String, Object> templateData,
        String from,
        String fromName,
        SmtpAccount smtp
) {

    public MailQueueMessage {
        cc = cc == null ? Collections.emptyList() : List.copyOf(cc);
        bcc = bcc == null ? Collections.emptyList() : List.copyOf(bcc);
        templateData = templateData == null ? Collections.emptyMap() : Map.copyOf(templateData);
    }
}
