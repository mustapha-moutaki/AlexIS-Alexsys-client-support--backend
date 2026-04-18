package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.ticket.*;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketDetailDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.ticket.TicketSummaryDtoResponse;
import org.springframework.data.domain.Page;

public interface TicketService {

    TicketDetailDtoResponse create(TicketCreateCommand command);

    TicketSummaryDtoResponse update(TicketCreateCommand command);

    TicketSummaryDtoResponse getByIdSummary(Long id);
    TicketDetailDtoResponse getDetailsById(Long id);

    Page<TicketSummaryDtoResponse>getAllTicketsSummary(int page, int size);
    Page<TicketDetailDtoResponse> getAllTicketDetailed(int page, int size);

    void delete(Long id);
}
