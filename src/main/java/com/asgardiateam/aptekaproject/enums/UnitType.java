package com.asgardiateam.aptekaproject.enums;

import com.asgardiateam.aptekaproject.exception.AptekaException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UnitType {

    PACK, PIECE;

    public static UnitType tryFindUnitType(String unitType) {
        return Arrays.stream(UnitType.values())
                .filter(x -> x.name().equals(unitType))
                .findFirst()
                .orElseThrow(AptekaException::unitTypeNotValid);
    }

}
