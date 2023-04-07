package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum State {

    ALIVE("MAVJUD"), DELETED("TUGAGAN");

    private final String uzbName;

    public static State tryFindStatus(String text) {
        return Arrays.stream(State.values())
                .filter(x -> x.getUzbName().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::statusNotValid);
    }

}
