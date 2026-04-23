package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.agent.AgentDtoResponse;
import com.alexsysSolutions.alexsis.model.Agent;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AgentMapper {
    @Mapping(target = "specialization", source = "specialization")
    @Mapping(target = "availabilityStatus", source = "availabilityStatus")
    @Mapping(target = "level", source = "level")
    AgentDtoResponse toDto(Agent agent);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    void updateEntity(AgentUpdateDtoRequest dto, @MappingTarget Agent agent);

    Agent toEntity(AgentCreateDtoRequest dto);
}
