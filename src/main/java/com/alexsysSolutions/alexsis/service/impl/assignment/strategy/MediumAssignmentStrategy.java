package com.alexsysSolutions.alexsis.service.impl.assignment.strategy;

import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Component
public class MediumAssignmentStrategy implements AssignmentStrategy {


    @Override
    public Agent assign(List<Agent> agents) {

        return agents.stream()
                .sorted(Comparator.comparing(Agent::getActiveTicketsCount))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No available agent for MEDIUM priority"));
    }
}
