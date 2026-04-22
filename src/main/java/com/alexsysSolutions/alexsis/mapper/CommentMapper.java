package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.comment.CommentDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CommentMapper {


    @Mapping(source = "author.username", target = "authorName")
    @Mapping(source = "createdAt", target = "createdAt")
    CommentDtoResponse toDto(Comment comment);
    @Mappings({
            @Mapping(target ="ticket", ignore = true),
            @Mapping(target ="author", ignore = true)
    })
    Comment toEntity(CommentDtoRequest dto);

}
