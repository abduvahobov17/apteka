package com.asgardiateam.aptekaproject.payload;

import com.asgardiateam.aptekaproject.enums.DeliveryType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTO {

    private DeliveryType deliveryType;

    private String address;

    private Double lon;

    private Double lat;
}
