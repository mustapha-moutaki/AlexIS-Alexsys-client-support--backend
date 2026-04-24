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
    AND a.availabilityStatus = AvailabilityStatus.AVAILABLE
    AND a.activeTicketsCount < a.maxCapacity
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



    // ==== Queries for stats =====
    @Query("SELECT count(*) FROM Agent a")
    int countTotalAgents();

    @Query("SELECT count (*) FROM Agent a where a.availabilityStatus = 'BUSY' ")
    int countBusyAgent();

    @Query("SELECT count (*) FROM Agent a WHERE a.availabilityStatus = 'AVAILABLE' ")
    int countAvailableAgents();

    @Query("SELECT count(*) FROM Agent a WHERE a.activeTicketsCount > a.maxCapacity")
    int overloadAgents();

    @Query("SELECT avg (a.averageResolutionTime) FROM Agent a")
    Double averageResolutionTime();

    @Query("SELECT avg(a.performanceRating) FROM Agent a")
    Double averagePerformanceRating();

    @Query("SELECT a.firstName, a.lastName FROM Agent a" +
            " WHERE a.performanceRating = (SELECT max(performanceRating) FROM Agent) ")
    String findBestAgent();

    @Query("SELECT avg(a.activeTicketsCount) FROM Agent a")
    double avgLoadPerAgent();

    // riskDetected need to cal so in the service
}
