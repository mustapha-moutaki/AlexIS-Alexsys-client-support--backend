package com.alexsysSolutions.alexsis.service.strategy;

import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;

import java.util.List;

public interface AssignmentStrategy {
        User assign(List<Agent> agents);
    }
