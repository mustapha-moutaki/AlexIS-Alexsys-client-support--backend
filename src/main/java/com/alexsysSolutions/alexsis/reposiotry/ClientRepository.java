package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Client;
import com.alexsysSolutions.alexsis.model.Ticket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);
    Optional<Client>findByUsername(String username);
    Optional<Client> findById(Long id);

    @Query("""
SELECT c FROM Client c
WHERE (:isVip IS NULL OR c.isVip = :isVip)
  AND (:isActive IS NULL OR c.active = :isActive)
""")
    Page<Client> findAllWithFilters(
            @Param("isVip") Boolean isVip,
            @Param("isActive") Boolean isActive,
            Pageable pageable
    );

    // stats queries Overview:
    @Query("SELECT COUNT(c) FROM Client c")
    int countTotalClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.active = true AND c.deleted = false")
    int countActiveClients();

    @Query("SELECT COUNT(c) FROM Client c WHERE FUNCTION('DATE', c.registrationDate) = CURRENT_DATE")
    int countClientsRegisteredToday();

    @Query("SELECT AVG(c.satisfactionScore) FROM Client c")
    Double averageSatisfactionScore();

    @Query("SELECT COUNT(c) FROM Client c WHERE c.satisfactionScore < 3")
    int countLowSatisfactionClients();



    //  stats queries for client dashaboard


//    // ticket need attention
    @Query("""
    SELECT COUNT(t)
    FROM Ticket t
    WHERE t.client.id = :clientId
    AND t.priority = 'HIGH'
    AND t.status IN ('OPEN', 'IN_PROGRESS')
    """)
    ClientTicketsNeedingAttentionProjection countTicketsNeedingAttention(long clientId);



    @Query("""
    SELECT\s
    COUNT(t) as totalTickets,
   \s
    SUM(CASE WHEN t.status='OPEN' THEN 1 ELSE 0 END) as openTickets,
    SUM(CASE WHEN t.status='IN_PROGRESS' THEN 1 ELSE 0 END) as inProgressTickets,
    SUM(CASE WHEN t.status='RESOLVED' THEN 1 ELSE 0 END) as resolvedTickets,
    SUM(CASE WHEN t.status='CLOSED' THEN 1 ELSE 0 END) as closedTickets,
   \s
    SUM(CASE\s
        WHEN FUNCTION('DATE', t.createdAt) = CURRENT_DATE\s
        THEN 1 ELSE 0 END
    ) as ticketsCreatedToday
   \s
    FROM Ticket t
    WHERE t.client.id = :clientId
   \s""")
    ClientTicketStatusProjection getStatsOfTicketsStat(Long clientId);

// how jpa know with alias mapping i'm gonna search about it more

    @Query("""
    SELECT\s
    SUM(CASE WHEN t.priority = 'HIGH' THEN 1 ELSE 0 END) as highPriorityTickets,
    SUM(CASE WHEN t.priority = 'MEDIUM' THEN 1 ELSE 0 END) as mediumPriorityTickets,
    SUM(CASE WHEN t.priority = 'LOW' THEN 1 ELSE 0 END) as lowPriorityTickets
    FROM Ticket t
    WHERE t.client.id = :clientId
   \s""")
    ClientPriorityProjection getMyPriorityStats(Long clientId);

    @Query("SELECT c.registrationDate FROM Client c WHERE c.id = :clientId")
    LocalDateTime findRegistrationDateById(Long clientId);

}
