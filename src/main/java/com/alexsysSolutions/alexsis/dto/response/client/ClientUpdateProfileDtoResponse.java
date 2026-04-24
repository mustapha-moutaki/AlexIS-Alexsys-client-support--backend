package com.alexsysSolutions.alexsis.dto.response.client;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ClientUpdateProfileDtoResponse {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
}
