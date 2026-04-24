package com.alexsysSolutions.alexsis.dto.request.agent;

import com.alexsysSolutions.alexsis.enums.AgentLevel;
import com.alexsysSolutions.alexsis.enums.AvailabilityStatus;
import com.alexsysSolutions.alexsis.enums.Specialization;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AgentUpdateDtoRequest {


    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String profilePicture;
    private boolean active;
    private boolean deleted;

    private Specialization specialization;
    private Integer averageResolutionTime;
    private Integer performanceRating;

    private AvailabilityStatus availabilityStatus;
    private AgentLevel level;
}
