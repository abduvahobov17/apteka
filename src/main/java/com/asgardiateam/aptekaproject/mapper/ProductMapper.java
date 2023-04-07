package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.enums.Status;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", imports = {Status.class})
public interface ProductMapper  {


    @Mapping(target = "state", expression = "java(product.getState().getUzbName())")
    ProductDTO toDTO(Product product);

    @Mapping(target = "status", expression = "java(Status.ACTIVE)")
    @Mapping(target = "id", ignore = true)
    Product toCreate(ProductRequest request);

    @Mapping(target = "id", expression = "java(product.getId())")
    @Mapping(target = "status", expression = "java(product.getStatus())")
    Product toUpdate(ProductRequest productRequest, @MappingTarget Product product);

}
