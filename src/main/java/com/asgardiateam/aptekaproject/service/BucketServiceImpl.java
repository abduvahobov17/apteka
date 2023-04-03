package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.entity.Bucket;
import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.BucketCriteria;
import com.asgardiateam.aptekaproject.entity.dynamicquery.specifications.BucketSpecifications;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.exception.AptekaException;
import com.asgardiateam.aptekaproject.mapper.BucketMapper;
import com.asgardiateam.aptekaproject.payload.BucketDTO;
import com.asgardiateam.aptekaproject.repository.BucketRepository;
import com.asgardiateam.aptekaproject.service.interfaces.BucketService;
import com.asgardiateam.aptekaproject.utils.Page2Dto;
import com.asgardiateam.aptekaproject.utils.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BucketServiceImpl implements BucketService {

    private final BucketRepository bucketRepository;
    private final BucketMapper bucketMapper;

    @Override
    public PageDto<BucketDTO> getBuckets(BucketCriteria criteria, Pageable pageable) {
        return Page2Dto.toDTO(findAll(criteria, pageable).map(bucketMapper::toDTO));
    }

    @Override
    public Optional<Bucket> getBucketByUserId(Long userId) {
        return bucketRepository.findByUser_IdAndBucketStatus(userId, BucketStatus.PROGRESS);
    }

    @Override
    public Bucket save(Bucket bucket) {
        return bucketRepository.save(bucket);
    }

    private Bucket findById(Long id) {
        return bucketRepository.findById(id)
                .orElseThrow(AptekaException::bucketNotFound);
    }

    private Page<Bucket> findAll(BucketCriteria criteria, Pageable pageable) {
        return bucketRepository.findAll(BucketSpecifications.createSpecifications(criteria), pageable);
    }
}
