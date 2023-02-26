package com.asgardiateam.aptekaproject.utils;

public interface MapperService<DTO, ENTITY, REQUEST> {

    DTO toDTO(ENTITY entity);

    ENTITY toENTITY(DTO dto);

    ENTITY toCreate(REQUEST request);

    ENTITY toUpdate(REQUEST request, ENTITY entity);
}
