package com.asgardiateam.aptekaproject.payload;

import com.asgardiateam.aptekaproject.enums.BucketStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BucketDTO {

    private Long bucketId;

    private BucketStatus bucketStatus;

    private Double longitude;

    private Double latitude;

    private String paymentType;

    private ClientInfo clientInfo;

    private List<BucketProductDTO> products;
}
