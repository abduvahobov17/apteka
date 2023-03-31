package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.BucketProduct;

import java.util.List;
import java.util.Optional;

public interface BucketProductService {

    BucketProduct save(BucketProduct bucketProduct);

    BucketProduct findById(Long id);

    void deleteAll(List<BucketProduct> bucketProducts);

    void delete(BucketProduct bucketProduct);
}
