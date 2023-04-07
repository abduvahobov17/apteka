package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ClientType {

    REGISTERED("FAOL"), UNREGISTERED("O'CHIRILGAN");

    private final String uzbName;

    public static ClientType tryFindClientType(String text) {
        return Arrays.stream(ClientType.values())
                .filter(clientType -> clientType.name().equals(text))
                .findFirst()
                .orElseThrow(AptekaException::clientTypeNotValid);
    }

}
