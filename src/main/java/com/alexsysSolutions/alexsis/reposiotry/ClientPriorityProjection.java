package com.alexsysSolutions.alexsis.reposiotry;

public interface ClientPriorityProjection {
    int getHighPriorityTickets();

    int getMediumPriorityTickets();

    int getLowPriorityTickets();
}
