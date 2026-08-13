package com.pushnotification.validation;

import com.pushnotification.domain.NotificationChannel;
import com.pushnotification.dto.NotificationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

public class NotificationRequestValidator implements ConstraintValidator<ValidNotificationRequest, NotificationRequest> {

    @Override
    public boolean isValid(NotificationRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        boolean valid = true;
        context.disableDefaultConstraintViolation();

        if (request.channels().contains(NotificationChannel.MAIL)) {
            if (request.recipients() == null || !StringUtils.hasText(request.recipients().email())) {
                context.buildConstraintViolationWithTemplate("recipients.email is required when mail channel is selected")
                        .addPropertyNode("recipients")
                        .addConstraintViolation();
                valid = false;
            }
        }

        return valid;
    }
}
