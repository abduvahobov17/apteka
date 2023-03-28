package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Bucket;

import java.util.Optional;

public interface BucketService {

    Optional<Bucket> getBucketByUserId(Long userId);

    Bucket save(Bucket bucket);
}
