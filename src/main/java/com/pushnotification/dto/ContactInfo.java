package com.pushnotification.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record ContactInfo(
        @Size(max = 100) String firstName,
        @Size(max = 100) String lastName,
        @Email @Size(max = 255) String email,
        @Size(max = 30) String phone
) {
}
