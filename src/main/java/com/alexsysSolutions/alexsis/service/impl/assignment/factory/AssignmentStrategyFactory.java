package com.alexsysSolutions.alexsis.service.impl.assignment.factory;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.service.impl.assignment.strategy.CriticalAssignmentStrategy;
import com.alexsysSolutions.alexsis.service.impl.assignment.strategy.HighAssignmentStrategy;
import com.alexsysSolutions.alexsis.service.impl.assignment.strategy.LowAssignmentStrategy;
import com.alexsysSolutions.alexsis.service.impl.assignment.strategy.MediumAssignmentStrategy;
import com.alexsysSolutions.alexsis.service.strategy.AssignmentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssignmentStrategyFactory {

    private final CriticalAssignmentStrategy critical;
    private final HighAssignmentStrategy high;
    private final MediumAssignmentStrategy medium;
    private final LowAssignmentStrategy low;

    public AssignmentStrategy getStrategy(Priority priority) {
        return switch (priority) {
            case CRITICAL -> critical;
            case HIGH -> high;
            case MEDIUM -> medium;
            case LOW -> low;
        };
    }
}