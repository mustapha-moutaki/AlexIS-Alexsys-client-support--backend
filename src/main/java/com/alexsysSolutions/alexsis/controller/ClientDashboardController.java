package com.alexsysSolutions.alexsis.controller;


import com.alexsysSolutions.alexsis.dto.response.dashboard.DashboardOverViewDto;
import com.alexsysSolutions.alexsis.service.dashboardService.ClientDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard/client")
@RequiredArgsConstructor

public class ClientDashboardController {

    private final ClientDashboardService clientDashboardService;

    @GetMapping("/{clientId}")
    public ResponseEntity<DashboardOverViewDto> getClientDashboard(
            @PathVariable Long clientId
    ) {

        DashboardOverViewDto dto = new DashboardOverViewDto();

        // ticket stats
        var status = clientDashboardService.getTicketStats(clientId);
        dto.setCountMyTotalTickets(status.getTotalTickets());
        dto.setCountMyOpenTickets(status.getOpenTickets());
        dto.setCountMyInProgressTickets(status.getInProgressTickets());
        dto.setCountMyResolvedTickets(status.getResolvedTickets());
        dto.setMyClosedTickets(status.getClosedTickets());
        dto.setMyTicketsCreatedToday(status.getTicketsCreatedToday());

        // priority stats
        var priority = clientDashboardService.getTicketPriority(clientId);
        dto.setMyHighPriorityTickets(priority.getHighPriorityTickets());
        dto.setMyMediumPriorityTickets(priority.getMediumPriorityTickets());
        dto.setMyLowPriorityTickets(priority.getLowPriorityTickets());

        // extra stat (attention)
        var attentionNeededToTicket = clientDashboardService.getTicketsNeedingAttention(clientId);
        dto.setTicketsNeedingAttention(attentionNeededToTicket.getTicketsNeedingAttention());

        return ResponseEntity.ok(dto);
    }
}
