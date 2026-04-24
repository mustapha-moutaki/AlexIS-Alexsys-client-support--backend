package com.alexsysSolutions.alexsis.service.impl.assignment.strategy;

import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;


@Component
public class LowAssignmentStrategy implements AssignmentStrategy {

        @Override
        public Agent assign(List<Agent> agents) {
            return agents.stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("No available agent for LOW priority"));
        }
}
