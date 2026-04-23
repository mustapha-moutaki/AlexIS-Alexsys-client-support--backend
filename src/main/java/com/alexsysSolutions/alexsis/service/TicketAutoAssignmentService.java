package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.enums.Priority;
import com.alexsysSolutions.alexsis.model.User;

public interface TicketAutoAssignmentService {

    public User assignAgent(Priority priority) ;
}
