package com.asgardiateam.aptekaproject.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BucketStatus {

    PROGRESS, CANCEL, CONFIRMED, COMPLETED;

    public static BucketStatus tryFindByName(String name) {
        return Arrays.stream(BucketStatus.values())
                .filter(status -> status.name().equals(name))
                .findFirst()
                .orElse(null);
    }
}
