package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Lang {

    RU("Русский язык \uD83C\uDDF7\uD83C\uDDFA"),
    UZ("O'zbek tili \uD83C\uDDFA\uD83C\uDDFF"),
    UNKNOWN("unknown");

    private final String description;

    public static Lang findByDescription(String description) {
        return Arrays.stream(Lang.values())
                .filter(lang -> lang.getDescription().equals(description))
                .findFirst()
                .orElse(UNKNOWN);
    }

    public static Lang tryFindLang(String description) {
        return Arrays.stream(Lang.values())
                .filter(lang -> lang.getDescription().equals(description))
                .findFirst()
                .orElse(null);
    }

    public static Lang tryFindLangByText(String text) {
        return Arrays.stream(Lang.values())
                .filter(x -> x.name().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::langNotValid);
    }

}
