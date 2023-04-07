package com.asgardiateam.aptekaproject.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentType {

    CASH("NAQD"), CARD("KARTA");

    private final String uzbName;
}
