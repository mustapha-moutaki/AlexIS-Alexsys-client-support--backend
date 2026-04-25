package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Ticket;
import org.apache.commons.text.translate.NumericEntityUnescaper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // to avoid N+1 issue
    @Query("""
    SELECT t FROM Ticket t
    LEFT JOIN FETCH t.comments
    LEFT JOIN FETCH t.attachments
    LEFT JOIN FETCH t.category
    LEFT JOIN FETCH t.client
    LEFT JOIN FETCH t.assignedTo
    WHERE t.id = :id
    """)
    Optional<Ticket> findByIdWithDetails(Long id);

    boolean existsById(Long id);
    Page<Ticket>findByClientId(Long id, Pageable pageable);

  List<Ticket> findByAssignedToIsNullAndCreatedAtBefore(LocalDateTime time);

    @Query("""
    SELECT t FROM Ticket t
    WHERE t.assignedTo IS NULL
    AND t.createdAt IS NOT NULL
    """)
    List<Ticket> findUnassignedTickets();


    // for stats
    @Query("SELECT count (t) FROM Ticket t")
    int totalTickets();

    @Query("SELECT count(t) FROM Ticket t WHERE t.status = 'OPEN' OR t.status = 'IN_PROGRESS' OR t.status = 'REOPENED'")
    int totalActiveTickets();

    @Query("SELECT count(t) FROM Ticket t WHERE t.status='RESOLVED' ")
    int totalResolvedTickets();

    @Query("SELECT count(t) FROM Ticket t WHERE t.status='CLOSED' ")
    int totalClosedTickets();

    /*
    native query=true: the query is not jpQL it's Sql
    unit : hour
     */
    @Query(value = """
    SELECT AVG(EXTRACT(EPOCH FROM (t.resolved_at - t.created_at)) / 3600) 
    FROM tickets t
    WHERE t.status = 'RESOLVED' AND t.assigned_agent_id IS NOT NULL
    """, nativeQuery = true)
    Double avgResolutionTime();

    @Query("SELECT count(t) FROM Ticket t WHERE t.priority = 'HIGH' ")
    int highPriorityTickets();

    @Query("SELECT count(t) FROM Ticket t WHERE FUNCTION('DATE', t.createdAt) = CURRENT_DATE")
    int totalTicketsToday();

}
