package com.alexsysSolutions.alexsis.dto.response.dashboard;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ClientDashboardOverViewDtoResponse {
    private int countMyTotalTickets;
    private int countMyOpenTickets;
    private int countMyInProgressTickets;
    private int countMyResolvedTickets;
    private int myClosedTickets;

    private int myHighPriorityTickets;
    private int myMediumPriorityTickets;
    private int myLowPriorityTickets;


    // Profile Information
    private LocalDateTime registrationDate;

    private int ticketsNeedingAttention; // tickets with issues or close to SLA

}
