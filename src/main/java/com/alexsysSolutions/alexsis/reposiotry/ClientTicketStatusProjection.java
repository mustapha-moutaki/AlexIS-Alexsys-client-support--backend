package com.alexsysSolutions.alexsis.reposiotry;

public interface ClientTicketStatusProjection {

    int getTotalTickets();

    Integer getOpenTickets();

    Integer getInProgressTickets();

    Integer getResolvedTickets();

    Integer getClosedTickets();

    Integer getTicketsCreatedToday();


}

