package com.alexsysSolutions.alexsis.mapper;

import com.alexsysSolutions.alexsis.dto.request.ticket.*;
import com.alexsysSolutions.alexsis.dto.response.Category.CategoryLiteDto;
import com.alexsysSolutions.alexsis.dto.response.attachment.AttachmentDtoResponse;

import com.alexsysSolutions.alexsis.dto.response.comment.CommentDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import com.alexsysSolutions.alexsis.model.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    // =========================
    // ENTITY -> DTO (DETAIL)
    // =========================
    @Mappings({
            @Mapping(source = "category", target = "category"),
            @Mapping(target = "clientName", expression = "java(ticket.getClient().getFirstName() + \" \" + ticket.getClient().getLastName())"),
            @Mapping(target = "assignedToName", expression = "java(ticket.getAssignedTo() != null ? ticket.getAssignedTo().getFirstName() + \" \" + ticket.getAssignedTo().getLastName() : null)"),
            @Mapping(source = "comments", target = "comments"),
            @Mapping(source = "attachments", target = "attachments")
    })
    TicketDetailDtoResponse toDtoDetailsResponse(Ticket ticket);


    // =========================
    // ENTITY -> DTO (SUMMARY)
    // =========================
    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "client.id", target = "clientId"),
            @Mapping(source = "assignedTo.id", target = "assignedToId"),
            @Mapping(target = "commentCount", expression = "java(ticket.getComments() != null ? ticket.getComments().size() : 0)"),
            @Mapping(target = "attachmentCount", expression = "java(ticket.getAttachments() != null ? ticket.getAttachments().size() : 0)")
    })
    TicketSummaryDtoResponse toDtoSummaryResponse(Ticket ticket);


    // =========================
    // LIST MAPPING
    // =========================
    List<TicketSummaryDtoResponse> toDtoSummaryList(List<Ticket> tickets);


    // =========================
    // CREATE (ADMIN)
    // =========================
    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "assignedTo", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "attachments", ignore = true),
            @Mapping(target = "assignedAt", ignore = true),
            @Mapping(target = "resolvedAt", ignore = true),
            @Mapping(target = "closedAt", ignore = true)
    })
    Ticket toEntityByAdmin(TicketCreateByAdminDto dto);


    // =========================
    // CREATE (CLIENT)
    // =========================
    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "assignedTo", ignore = true),
            @Mapping(target = "status", ignore = true), // set in service
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "attachments", ignore = true),
            @Mapping(target = "assignedAt", ignore = true),
            @Mapping(target = "resolvedAt", ignore = true),
            @Mapping(target = "closedAt", ignore = true)
    })
    Ticket toEntityByClient(TicketCreateByClientDto dto);


    // =========================
    // UPDATE (ADMIN)
    // =========================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "category", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "assignedTo", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "attachments", ignore = true)
    })
    void toUpdateByAdmin(TicketUpdateByAdminDtoRequest dto, @MappingTarget Ticket ticket);


    // =========================
    // UPDATE (CLIENT)
    // =========================
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mappings({
            @Mapping(target = "status", ignore = true),
            @Mapping(target = "assignedTo", ignore = true),
            @Mapping(target = "client", ignore = true),
            @Mapping(target = "comments", ignore = true),
            @Mapping(target = "attachments", ignore = true)
    })
    void toUpdateByClient(TicketUpdateByClientDtoRequest dto, @MappingTarget Ticket ticket);


    // =========================
    // NESTED MAPPINGS
    // =========================
    CategoryLiteDto toCategoryLiteDto(Category category);

    CommentDtoResponse toCommentDto(Comment comment);

    List<CommentDtoResponse> toCommentList(List<Comment> comments);

    AttachmentDtoResponse toAttachmentDto(Attachment attachment);

    List<AttachmentDtoResponse> toAttachmentList(List<Attachment> attachments);
}