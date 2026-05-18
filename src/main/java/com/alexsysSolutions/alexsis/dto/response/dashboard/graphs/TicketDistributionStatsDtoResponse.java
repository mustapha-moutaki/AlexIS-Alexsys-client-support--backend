package com.alexsysSolutions.alexsis.dto.response.dashboard.graphs;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter

public class TicketDistributionStatsDtoResponse {
    private int open;
    private int assigned;
    private int inProgress;
    private int resolved;
    private int closed;
    private int reOpen;
}
