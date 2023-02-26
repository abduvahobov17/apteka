package com.asgardiateam.aptekaproject.utils;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PageDto<T> {

    private int pageNumber;

    private boolean previous;

    private boolean next;

    private int size;

    private long totalElements;

    private int totalPages;

    private List<T> items;

}