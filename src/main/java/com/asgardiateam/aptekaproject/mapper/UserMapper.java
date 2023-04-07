package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {


    @Mapping(target = "registeredDate", expression = "java(user.getCreatedDate().toEpochMilli())")
    @Mapping(target = "clientType", expression = "java(user.getClientType().getUzbName())")
    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toCreate(UserRequest request);

    User toUpdate(UserRequest request, @MappingTarget User user);

}
