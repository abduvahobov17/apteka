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

    public static AptekaException productNotFound() {
        return new AptekaException(PRODUCT_NOT_FOUND);
    }

    public static AptekaException productSaveError() {
        return new AptekaException(PRODUCT_SAVE_ERROR);
    }

    public static AptekaException productDeleteError() {
        return new AptekaException(PRODUCT_DELETE_ERROR);
    }

    public static AptekaException unauthorized() {
        return new AptekaException(UNAUTHORIZED);
    }

    public static AptekaException unitTypeNotValid() {
        return new AptekaException(UNIT_TYPE_NOT_VALID);
    }

    public static AptekaException statusNotValid() {
        return new AptekaException(STATUS_NOT_VALID);
    }

    public static AptekaException clientTypeNotValid() {
        return new AptekaException(CLIENT_TYPE_NOT_VALID);
    }

    public static AptekaException botStateNotValid() {
        return new AptekaException(BOT_STATE_NOT_VALID);
    }

    public static AptekaException bucketProductNotFound() {
        return new AptekaException(BUCKET_PRODUCT_NOT_FOUND);
    }

    public static AptekaException langNotValid() {
        return new AptekaException(LANG_NOT_VALID);
    }

    public static AptekaException deleteUserException() {
        return new AptekaException(USER_DELETE_EXCEPTION);
    }

    public static AptekaException bucketNotFound() {
        return new AptekaException(BUCKET_NOT_FOUND);
    }

    public static AptekaException saveError() {
        return new AptekaException(SAVE_ERROR);
    }

    public static AptekaException excelError() {
        return new AptekaException(EXCEL_ERROR);
    }
}
