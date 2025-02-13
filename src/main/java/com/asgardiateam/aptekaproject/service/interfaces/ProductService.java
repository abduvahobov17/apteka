package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Product;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.payload.ProductDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.utils.CrudService;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService extends CrudService<Product, ProductDTO, ProductRequest, Long, ProductCriteria> {
    Optional<Product> findByName(String name);

    byte[] generateExcelProduct(ProductCriteria criteria, Pageable pageable);
}
