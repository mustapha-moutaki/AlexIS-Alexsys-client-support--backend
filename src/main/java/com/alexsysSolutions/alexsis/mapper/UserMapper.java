package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import com.alexsysSolutions.alexsis.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE) // actice and delete are unknown so tell mapstrctu to ignore any unmapped target
public interface UserMapper {

    UserDtoResponse toDto(User user);

    User toEntity(UserCreateDtoRequest dto);

    void updateEntity(UserUpdateDtoRequest dto, @MappingTarget User user);
}
