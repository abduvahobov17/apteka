package com.asgardiateam.aptekaproject.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<ENTITY, DTO, REQUEST, ID, CRITERIA> {

    DTO create(REQUEST request);

    DTO update(REQUEST request, ID id);

    DTO getById(ID id);

    PageDto<DTO> getAll(Pageable pageable, CRITERIA criteria);

    ENTITY findById(ID id);

    Page<ENTITY> findAll(Pageable pageable, CRITERIA criteria);

    ENTITY save(ENTITY entity);

    void delete(ENTITY entity);

}
