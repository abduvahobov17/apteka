package com.asgardiateam.aptekaproject.repository;

import com.asgardiateam.aptekaproject.entity.BucketProduct;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketProductRepository extends JpaRepositoryImplementation<BucketProduct, Long> {
}
