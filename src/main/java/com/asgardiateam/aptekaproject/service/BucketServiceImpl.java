package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.repository.BucketRepository;
import com.asgardiateam.aptekaproject.service.interfaces.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;

    @Override
    public Optional<Bucket> getBucketByUserId(Long userId) {
        return bucketRepository.findByUser_IdAndBucketStatus(userId, BucketStatus.PROGRESS);
    }

    @Override
    public Bucket save(Bucket bucket) {
        return bucketRepository.save(bucket);
    }
}
