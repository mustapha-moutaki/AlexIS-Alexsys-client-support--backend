package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.dto.response.dashboard.AgentDashboardOverviewDtoResponse;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.dashboardService.IAgentDashboardService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
@Setter
@Service

public class AgentDashboardServiceImpl implements IAgentDashboardService {

    private final CurrentUserProvider currentUserProvider;
    private final Logger logger = LoggerFactory.getLogger(AgentDashboardServiceImpl.class);

    @Override
    public AgentDashboardOverviewDtoResponse getAgentDashboardOverview() throws AccessDeniedException {
        logger.info("getting the current user");
        User user =  currentUserProvider.getCurrentUser().getUser();
        if(user.getRole() != UserRole.AGENT){
            throw new AccessDeniedException("Only agent allowed");
        }
        Agent agent = (Agent) user;

        logger.info("current user data:  {}", user);

        return AgentDashboardOverviewDtoResponse.builder()
                .availabilityStatus(agent.getAvailabilityStatus().name())
                .activeTicketCount(Optional.ofNullable(agent.getActiveTicketsCount()).orElse(0))
                .avgResolutionTimeMin(Optional.ofNullable(agent.getAverageResolutionTime()).orElse(0))
                .specialization(agent.getSpecialization().name())
                .build();

    }
}
