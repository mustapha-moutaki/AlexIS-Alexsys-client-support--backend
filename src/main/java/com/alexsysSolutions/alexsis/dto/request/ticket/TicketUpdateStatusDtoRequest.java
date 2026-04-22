package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TicketUpdateStatusDtoRequest {

    @NotNull(message = "status is required")
    private TicketStatus status;
}
