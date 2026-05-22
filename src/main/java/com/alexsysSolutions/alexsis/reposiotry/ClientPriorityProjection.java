package com.alexsysSolutions.alexsis.reposiotry;

public interface ClientPriorityProjection {
    Integer getHighPriorityTickets();

    Integer getMediumPriorityTickets();

    Integer getLowPriorityTickets();
}
