package com.pushnotification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SmtpAccount(
        @NotBlank @Size(max = 200) String host,
        Integer port,
        @NotBlank @Size(max = 160) String username,
        @NotBlank @Size(max = 200) String password,
        Boolean ssl,
        Boolean starttls
) {

    @Override
    public String toString() {
        return "SmtpAccount[host=%s, port=%s, username=%s, ssl=%s, starttls=%s]"
                .formatted(host, port, username, ssl, starttls);
    }
}
