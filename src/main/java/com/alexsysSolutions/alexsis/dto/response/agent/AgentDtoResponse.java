package com.alexsysSolutions.alexsis.dto.response.agent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AgentDtoResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private boolean active;
    private boolean deleted;

    private String specialization;
    private Integer averageResolutionTime;
    private Integer performanceRating;

    private String availabilityStatus;
    private String level;
}
