package com.alexsysSolutions.alexsis.dto.request.Client;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ClientUpdateProfileDtoRequest {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String profilePicture;
    private String password;
}