package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.payload.AdminDTO;
import com.asgardiateam.aptekaproject.payload.request.AdminRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin toUpdate(AdminRequest request, @MappingTarget Admin admin);

    AdminDTO toDTO(Admin admin);
}
