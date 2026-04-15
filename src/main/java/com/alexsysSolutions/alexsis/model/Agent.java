package com.alexsysSolutions.alexsis.model;

import com.alexsysSolutions.alexsis.enums.AvailabilityStatus;
import com.alexsysSolutions.alexsis.enums.Specialization;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Agent extends User{

    private Specialization specialization;
    private String averageResolutionTime;
    private String performanceRating;
    private AvailabilityStatus availabilityStatus;

}
