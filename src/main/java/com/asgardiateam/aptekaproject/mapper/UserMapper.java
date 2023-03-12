package com.asgardiateam.aptekaproject.mapper;

import com.asgardiateam.aptekaproject.entity.User;
import com.asgardiateam.aptekaproject.payload.UserDTO;
import com.asgardiateam.aptekaproject.payload.request.UserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDTO(User user);

    @Mapping(target = "id", ignore = true)
    User toCreate(UserRequest request);

    User toUpdate(UserRequest request, @MappingTarget User user);

}
