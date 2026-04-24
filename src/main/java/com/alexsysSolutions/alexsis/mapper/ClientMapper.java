package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.Client.ClientCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateProfileDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.client.ClientDtoResponse;
import com.alexsysSolutions.alexsis.model.Client;
import lombok.Getter;
import lombok.Setter;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClientMapper {

    ClientDtoResponse toDto(Client client);

//    @Mapping(target = "registrationDate", ignore = true)
//    @Mapping(target = "satisfactionScore", ignore = true)
//    @Mapping(target = "lastInteractAt", ignore = true)
//    @Mapping(target = "isVip", ignore = true)
//    Client toEntity(ClientCreateDtoRequest dto);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Client updateClientByAdmin(ClientUpdateByAdminDtoRequest dto, @MappingTarget Client client);
//
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    Client updateClientProfile(ClientUpdateProfileDtoRequest dto, @MappingTarget Client client);
}
