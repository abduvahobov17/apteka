package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ClientType {

    REGISTERED, UNREGISTERED;

    public static ClientType tryFindClientType(String text) {
        return Arrays.stream(ClientType.values())
                .filter(clientType -> clientType.name().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::clientTypeNotValid);
    }

}
