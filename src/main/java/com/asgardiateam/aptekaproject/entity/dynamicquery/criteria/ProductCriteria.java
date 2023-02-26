package com.asgardiateam.aptekaproject.entity.dynamicquery.criteria;

import com.asgardiateam.aptekaproject.common.deserializer.UnitTypeDeserializer;
import com.asgardiateam.aptekaproject.enums.UnitType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
public class ProductCriteria {

    private Long id;

    private String name;

    @Builder.Default
    private Long fromAmount = 0L;

    private Long toAmount;

    private String description;

    @Builder.Default
    private Long fromPrice = 0L;

    private Long toPrice;

    @JsonDeserialize(using = UnitTypeDeserializer.class)
    private UnitType unitType;

}
