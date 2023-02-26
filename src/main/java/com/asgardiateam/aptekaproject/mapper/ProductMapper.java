package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.utils.MapperService;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper extends MapperService<ProductDTO, Product, ProductRequest> {

}
