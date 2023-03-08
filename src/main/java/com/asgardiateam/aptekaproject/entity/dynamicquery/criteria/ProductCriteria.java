package com.asgardiateam.aptekaproject.entity.dynamicquery.criteria;

import com.asgardiateam.aptekaproject.common.deserializer.UnitTypeDeserializer;
import com.asgardiateam.aptekaproject.enums.UnitType;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ProductCriteria {

    private Long id;

    private String name;

    private Long fromAmount;

    private Long toAmount;

    private String description;

    private Long fromPrice;

    private Long toPrice;

    @JsonDeserialize(using = UnitTypeDeserializer.class)
    private UnitType unitType;

    public ProductCriteria() {
        this.fromAmount = 0L;
        this.fromPrice = 0L;
    }
}
