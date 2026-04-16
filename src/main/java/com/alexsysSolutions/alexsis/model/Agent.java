package com.alexsysSolutions.alexsis.model;

import com.alexsysSolutions.alexsis.enums.AvailabilityStatus;
import com.alexsysSolutions.alexsis.enums.Specialization;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Agent extends User{

    @Column(name = "specialization", nullable = false)
    @Enumerated(EnumType.STRING)
    private Specialization specialization;

    @Column(name = "average_resolution_time")
    private String averageResolutionTime;
    @Column(name = "performance_rating")
    private String performanceRating;

    @Column(name = "availability_status")
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus availabilityStatus;

}
