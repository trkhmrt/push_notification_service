package com.pushnotification.dto;

import jakarta.validation.constraints.Email;

import java.util.Collections;
import java.util.List;

public record Recipients(
        @Email String email,
        List<@Email String> cc,
        List<@Email String> bcc
) {

    public Recipients {
        cc = cc == null ? Collections.emptyList() : List.copyOf(cc);
        bcc = bcc == null ? Collections.emptyList() : List.copyOf(bcc);
    }
}
