package com.alexsysSolutions.alexsis.dto.response.dashboard;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentDashboardOverviewDtoResponse {

    private String availabilityStatus;
    private int activeTicketCount;
    private long avgResolutionTimeMin;
    private String specialization;


}
