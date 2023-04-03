package com.asgardiateam.aptekaproject.entity.dynamicquery.specifications;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.BucketCriteria;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class BucketSpecifications {

    public static Specification<Bucket> createSpecifications(BucketCriteria bucketCriteria) {
        return createdDateAround(bucketCriteria.getFromDate(), bucketCriteria.getToDate())
                .and(bucketStatusEquals(bucketCriteria.getBucketStatus()));
    }

    public static Specification<Bucket> createdDateAround(Instant fromDate, Instant toDate) {
        return (root, query, criteriaBuilder) -> toDate != null ?
                criteriaBuilder.between(root.get("createdDate"), fromDate, toDate) :
                criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), fromDate);
    }

    public static Specification<Bucket> bucketStatusEquals(BucketStatus status) {
        return (root, query, criteriaBuilder) -> status != null ? criteriaBuilder.equal(root.get("bucketStatus"), status)
                : null;
    }
}
