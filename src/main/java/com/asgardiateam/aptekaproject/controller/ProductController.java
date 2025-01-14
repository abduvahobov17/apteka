package com.asgardiateam.aptekaproject.controller;

import com.asgardiateam.aptekaproject.entity.dynamicquery.criteria.ProductCriteria;
import com.asgardiateam.aptekaproject.enums.Status;
import com.asgardiateam.aptekaproject.enums.UnitType;
import com.asgardiateam.aptekaproject.payload.MessageDTO;
import com.asgardiateam.aptekaproject.payload.request.ProductRequest;
import com.asgardiateam.aptekaproject.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.List;

import static com.asgardiateam.aptekaproject.common.ResponseData.ok;
import static com.asgardiateam.aptekaproject.constants.ApiConstants.*;
import static com.asgardiateam.aptekaproject.constants.MessageKey.SUCCESS_MESSAGE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = API_V1 + PRODUCTS, produces = APPLICATION_JSON_VALUE)
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

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public Object create(@RequestBody @Valid ProductRequest request) {
        return ok(productService.create(request));
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, path = "/{productId}")
    public Object update(@RequestBody @Valid ProductRequest request,
                         @PathVariable Long productId) {
        return ok(productService.update(request, productId));
    }

    @DeleteMapping("/{productId}")
    public Object delete(@PathVariable Long productId) {
        productService.deleteById(productId);
        return ok(new MessageDTO(SUCCESS_MESSAGE));
    }

    @GetMapping(EXCEL)
    public Object excel(@PageableDefault(sort = "createdDate", direction = Sort.Direction.DESC, size = 20) Pageable pageable,
                        ProductCriteria criteria) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-type", "application/vnd.ms-excel");
        headers.set("Content-Disposition", "attachment; filename=\"product_excel.xlsx\"");
        byte[] bytes = productService.generateExcelProduct(criteria, pageable);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @GetMapping(UNIT_TYPES)
    public Object getUnitTypes() {
        return ok(List.of(UnitType.values()));
    }

    @GetMapping(STATUSES)
    public Object getStatuses() {
        return ok(List.of(Status.values()));
    }
}
