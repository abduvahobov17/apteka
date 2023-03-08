package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.UnitType;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static com.asgardiateam.aptekaproject.constants.ApiConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = API_V1 + PRODUCTS, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public Object getAll(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
                         ProductCriteria productCriteria) {
        return ok(productService.getAll(pageable, productCriteria));
    }

    @GetMapping("/{productId}")
    public Object getById(@PathVariable Long productId) {
        return ok(productService.getById(productId));
    }

    @PostMapping
    public Object create(@RequestBody @Valid ProductRequest request) {
        return ok(productService.create(request));
    }

    @PutMapping("/{productId}")
    public Object update(@RequestBody @Valid ProductRequest request,
                         @PathVariable Long productId) {
        return ok(productService.update(request, productId));
    }

    @GetMapping(UNIT_TYPES)
    public Object getUnitTypes() {
        return ok(List.of(UnitType.values()));
    }
}
