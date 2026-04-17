package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.category.CategoryDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.Category.CategoryDtoResponse;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryDtoResponse toDto(Category category);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(CategoryDtoRequest dto, @MappingTarget Category category);


    Category toEntity(CategoryDtoRequest dto);

}
