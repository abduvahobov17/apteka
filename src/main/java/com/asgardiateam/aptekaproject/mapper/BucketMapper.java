package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.BucketProduct;
import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.payload.BucketDTO;
import com.asgardiateam.aptekaproject.payload.BucketProductDTO;
import com.asgardiateam.aptekaproject.payload.ClientInfo;
import com.asgardiateam.aptekaproject.payload.ProductInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface BucketMapper {

    @Mapping(target = "products", source = "bucket", qualifiedByName = "toProducts")
    @Mapping(target = "longitude", source = "lon")
    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "clientInfo", source = "bucket", qualifiedByName = "userToClientInfo")
    @Mapping(target = "bucketId", source = "id")
    BucketDTO toDTO(Bucket bucket);

    @Named("userToClientInfo")
    static ClientInfo toClientInfo(Bucket bucket) {
        User user = bucket.getUser();
        return ClientInfo.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Named("toProducts")
    static List<BucketProductDTO> toBucketProductDTO(Bucket bucket) {
        List<BucketProductDTO> bucketProductDTOS = new ArrayList<>();
        List<BucketProduct> bucketProducts = bucket.getBucketProducts();
        for (BucketProduct bucketProduct : bucketProducts) {
            BucketProductDTO bucketProductDTO = new BucketProductDTO();
            bucketProductDTO.setProductInfo(ProductInfo.builder()
                    .id(bucketProduct.getProduct().getId())
                    .amount(bucketProduct.getProduct().getAmount())
                    .description(bucketProduct.getProduct().getDescription())
                    .name(bucketProduct.getProduct().getName())
                    .price(bucketProduct.getProduct().getPrice())
                    .build());
            bucketProductDTO.setAmount(bucketProduct.getAmount());
            bucketProductDTO.setId(bucketProductDTO.getId());
            bucketProductDTOS.add(bucketProductDTO);
        }
        return bucketProductDTOS;
    }
}
