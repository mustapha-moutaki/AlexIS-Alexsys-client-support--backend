package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;

public interface TicketAutoAssignmentService {

    public Agent assignAgent(Priority priority) ;
}
