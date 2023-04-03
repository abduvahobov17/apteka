package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;

import java.util.Optional;

public interface BucketRepository extends JpaRepositoryImplementation<Bucket, Long> {

    Optional<Bucket> findByUser_IdAndBucketStatus(Long userId, BucketStatus status);
}
