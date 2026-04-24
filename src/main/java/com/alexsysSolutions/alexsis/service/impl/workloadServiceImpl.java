package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.service.WorkloadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class workloadServiceImpl implements WorkloadService {

    @Override
    public void incrementTicketsActiveCount(Agent agent) {
        //  If an agent's activeTicketsCount is null (not initialized), this throws NullPointerException
        int currentCount = agent.getActiveTicketsCount() != null ? agent.getActiveTicketsCount() : 0;
        agent.setActiveTicketsCount(Math.max(0, currentCount + 1));
        agent.setLastAssignedAt(LocalDateTime.now());
    }

    @Override
    public void decrementTicketsActiveCount(Agent agent) {
        int currentCount = agent.getActiveTicketsCount() != null ? agent.getActiveTicketsCount() : 0;
        agent.setActiveTicketsCount(Math.max(0, currentCount - 1));
        agent.setLastAssignedAt(LocalDateTime.now());
    }
}
