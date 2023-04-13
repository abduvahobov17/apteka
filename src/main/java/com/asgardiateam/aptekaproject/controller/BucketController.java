package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.BucketCriteria;
import com.asgardiateam.aptekaproject.enums.BucketStatus;
import com.asgardiateam.aptekaproject.service.interfaces.BucketService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.List;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static com.asgardiateam.aptekaproject.constants.ApiConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = API_V1 + BUCKETS, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BucketController {

    private final BucketService bucketService;

    @GetMapping
    public Object getAll(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                         BucketCriteria criteria) {
        return ok(bucketService.getBuckets(criteria, pageable));
    }

    @GetMapping(EXCEL)
    public ResponseEntity<?> excel(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                                BucketCriteria criteria) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/vnd.ms-excel");
        headers.set("Content-Disposition", "attachment; filename=\"product_excel.xlsx\"");
        byte[] bytes = bucketService.generateExcel(criteria, pageable);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping(BUCKET_STATUSES)
    public Object getBucketStatus() {
        return ok(List.of(BucketStatus.values()));
    }
}
