package com.asgardiateam.aptekaproject.utils;

import org.springframework.data.domain.Page;

public class Page2Dto {

    public static <T> PageDto<T> toDTO(Page<T> page) {
        return PageDto.<T>builder()
                .next(page.hasNext())
                .previous(page.hasPrevious())
                .items(page.getContent())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .pageNumber(page.getNumber())
                .totalPages(page.getTotalPages())
                .build();
    }

}