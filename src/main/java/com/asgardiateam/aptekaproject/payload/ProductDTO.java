package com.asgardiateam.aptekaproject.payload;

import com.asgardiateam.aptekaproject.enums.State;
import com.asgardiateam.aptekaproject.enums.Status;
import com.asgardiateam.aptekaproject.enums.UnitType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ProductDTO {

    private Long id;

    private String name;

    private Long amount;

    private String phoneNumber;

    private String supplier;

    private String state;

    private String description;

    private Long price;

    private UnitType unitType;

}
