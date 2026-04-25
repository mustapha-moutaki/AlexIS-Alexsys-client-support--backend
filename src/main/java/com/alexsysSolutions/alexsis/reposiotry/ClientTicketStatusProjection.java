package com.alexsysSolutions.alexsis.reposiotry;

public interface ClientTicketStatusProjection {

    int getTotalTickets();

    int getOpenTickets();

    int getInProgressTickets();

    int getResolvedTickets();

    int getClosedTickets();

    int getTicketsCreatedToday();


}

