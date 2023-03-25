package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;

import java.util.Arrays;

public enum BotState {

    START,
    REGISTER_LANG,
    REGISTER_NAME,
    REGISTER_PHONE,
    MAIN_MENU,
    SEARCH_PRODUCT_START,
    SEARCH_PRODUCT_PROGRESS,
    SEARCH_PRODUCT_CONFIRM,
    SETTINGS,
    ADD_PRODUCT,
    CONFIRM_BUCKET;

    public static BotState tryFindBotState(String text) {
        return Arrays.stream(BotState.values())
                .filter(x -> x.name().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::botStateNotValid);
    }
}
