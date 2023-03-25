package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Status {

    ACTIVE, NOT_ACTIVE;

    public static Status tryFindStatus(String text) {
        return Arrays.stream(Status.values())
                .filter(x -> x.name().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::statusNotValid);
    }
}
