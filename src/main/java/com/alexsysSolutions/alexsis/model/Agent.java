package com.alexsysSolutions.alexsis.model;

import com.alexsysSolutions.alexsis.enums.AgentLevel;
import com.alexsysSolutions.alexsis.enums.AvailabilityStatus;
import com.alexsysSolutions.alexsis.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

@SQLDelete(sql = "UPDATE agents SET is_deleted = true, is_active = false WHERE id = ?")
public class Agent extends User{

    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @Column(name = "average_resolution_time")
    private Integer averageResolutionTime;
    @Column(name = "performance_rating")
    private Integer performanceRating;

    @Column(name = "availability_status")
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

    // last added fields
    @Column(name = "active_tickets_count")
    private Integer activeTicketsCount;

    private Integer maxCapacity;
    private LocalDateTime lastAssignedAt;
    private AgentLevel level;

}
