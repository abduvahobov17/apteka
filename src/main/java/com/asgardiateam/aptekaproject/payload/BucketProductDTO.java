package com.asgardiateam.aptekaproject.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BucketProductDTO {

    private Long id;

    private Long amount;

    private ProductInfo productInfo;

}
