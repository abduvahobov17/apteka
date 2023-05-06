package com.asgardiateam.aptekaproject.service;

import com.asgardiateam.aptekaproject.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticService {

    private final UserService userService;
    private final BucketService bucketService;
    private final ProductService productService;
    private final BucketProductService bucketProductService;
}
