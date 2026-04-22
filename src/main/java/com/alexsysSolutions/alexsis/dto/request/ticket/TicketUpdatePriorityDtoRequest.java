package com.alexsysSolutions.alexsis.dto.request.ticket;

import com.alexsysSolutions.alexsis.enums.Priority;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TicketUpdatePriorityDtoRequest {
    @NotNull(message = "Priority is required")
    private Priority priority;

}
