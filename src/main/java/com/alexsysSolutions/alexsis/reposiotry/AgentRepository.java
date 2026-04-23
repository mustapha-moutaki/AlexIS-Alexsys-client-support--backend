package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    Optional<Agent>findByEmail(String email);
    Optional<Agent>findByUsername(String username);

    @Query("""
    SELECT a FROM Agent a
    WHERE a.active = true
    AND a.availabilityStatus = com.alexsysSolutions.alexsis.enums.AvailabilityStatus.AVAILABLE
    AND a.activeTicketsCount < a.maxCapacity
    AND a.lastAssignedAt IS NOT NULL
    ORDER BY a.activeTicketsCount ASC, a.lastAssignedAt ASC
    """)
    List<Agent> findAvailableAgents();

    // This method retrieves all available agents who:
    // - are active
    // - have availability status = AVAILABLE
    // - still have capacity to take new tickets (activeTicketsCount < maxCapacity)
    // - have been assigned at least once (lastAssignedAt is not null)
    // Then it sorts them by:
    // - lowest workload first (activeTicketsCount)
    // - and oldest assignment time first (fair distribution)

}
