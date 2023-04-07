package com.asgardiateam.aptekaproject.service.interfaces;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.BucketCriteria;
import com.asgardiateam.aptekaproject.payload.BucketDTO;
import com.asgardiateam.aptekaproject.utils.PageDto;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BucketService {

    PageDto<BucketDTO> getBuckets(BucketCriteria criteria, Pageable pageable);

    Optional<Bucket> getBucketByUserId(Long userId);

    Bucket save(Bucket bucket);

    byte[] generateExcel(BucketCriteria criteria, Pageable pageable);
}
