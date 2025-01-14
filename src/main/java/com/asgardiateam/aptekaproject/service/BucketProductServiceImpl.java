package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.BucketProduct;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.repository.BucketProductRepository;
import com.asgardiateam.aptekaproject.service.interfaces.BucketProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BucketProductServiceImpl implements BucketProductService {

    private final BucketProductRepository bucketProductRepository;

    @Override
    public BucketProduct save(BucketProduct bucketProduct) {
        return bucketProductRepository.save(bucketProduct);
    }

    @Override
    public BucketProduct findById(Long id) {
        return bucketProductRepository.findById(id)
                .orElseThrow(AptekaException::bucketProductNotFound);
    }

    @Override
    public void deleteAll(List<BucketProduct> bucketProducts) {
        bucketProductRepository.deleteAll(bucketProducts);
    }

    @Override
    public void delete(BucketProduct bucketProduct) {
        bucketProductRepository.delete(bucketProduct);
    }
}
