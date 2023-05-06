package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toUpdate(AdminRequest request, @MappingTarget Admin admin);

    @Mapping(target = "photoUrl", expression = "java(photoUrl)")
    AdminDTO toDTO(Admin admin, @Context String photoUrl);
}
