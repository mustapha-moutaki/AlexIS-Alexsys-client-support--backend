package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.model.Agent;
import org.springframework.stereotype.Service;


public interface WorkloadService {
    void incrementTicketsActiveCount(Agent agent);
    void decrementTicketsActiveCount(Agent agent);
}
