package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper  {


    ProductDTO toDTO(Product product);

    @Mapping(target = "id", ignore = true)
    Product toCreate(ProductRequest request);

    Product toUpdate(ProductRequest productRequest, @MappingTarget Product product);

}
