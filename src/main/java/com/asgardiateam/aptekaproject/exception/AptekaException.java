package com.asgardiateam.aptekaproject.exception;

import lombok.Getter;

import static com.asgardiateam.aptekaproject.constants.MessageKey.*;

@Getter
public class AptekaException extends RuntimeException {

    private final String message;

    public AptekaException(String message) {
       this.message = message;
    }

    public static AptekaException userNotFoundByTelegramId() {
        return new AptekaException(USER_NOT_FOUND_BY_TELEGRAM_ID);
    }

    public static AptekaException userNotFoundById() {
        return new AptekaException(USER_NOT_FOUND_BY_ID);
    }

    public static AptekaException badCredentials() {
        return new AptekaException(BAD_CREDENTIALS);
    }

}
