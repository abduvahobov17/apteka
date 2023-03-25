package com.asgardiateam.aptekaproject.payload.request;

import com.asgardiateam.aptekaproject.common.deserializer.StatusDeserializer;
import com.asgardiateam.aptekaproject.common.deserializer.UnitTypeDeserializer;
import com.asgardiateam.aptekaproject.enums.Status;
import com.asgardiateam.aptekaproject.enums.UnitType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import static com.asgardiateam.aptekaproject.constants.MessageKey.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductRequest {

    @NotEmpty(message = NAME_NOT_VALID)
    private String name;

    @PositiveOrZero(message = AMOUNT_NOT_VALID)
    private String amount;

    @NotEmpty(message = DESCRIPTION_NOT_VALID)
    private String description;

    @NotEmpty(message = SUPPLIER_NOT_VALID)
    private String supplier;

    @NotEmpty(message = PHONE_NUMBER_NOT_VALID)
    private String phoneNumber;

    @JsonDeserialize(using = StatusDeserializer.class)
    private Status status;

    @PositiveOrZero(message = PRICE_NOT_VALID)
    private Long price;

    @JsonDeserialize(using = UnitTypeDeserializer.class)
    private UnitType unitType;
}
