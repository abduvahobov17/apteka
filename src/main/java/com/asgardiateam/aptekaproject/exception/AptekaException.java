package com.asgardiateam.aptekaproject.exception;

import lombok.Getter;

import static com.asgardiateam.aptekaproject.constants.MessageKey.USER_NOT_FOUND_BY_ID;
import static com.asgardiateam.aptekaproject.constants.MessageKey.USER_NOT_FOUND_BY_TELEGRAM_ID;

@Getter
public class AptekaException extends RuntimeException {

    private String message;

    public AptekaException(String message) {
        super(message);
    }

    public static AptekaException userNotFoundByTelegramId() {
        return new AptekaException(USER_NOT_FOUND_BY_TELEGRAM_ID);
    }

    public static AptekaException userNotFoundById() {
        return new AptekaException(USER_NOT_FOUND_BY_ID);
    }

}
