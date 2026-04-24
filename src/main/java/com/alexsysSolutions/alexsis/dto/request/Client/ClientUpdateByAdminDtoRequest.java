package com.alexsysSolutions.alexsis.dto.request.Client;

import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientUpdateByAdminDtoRequest {
    private String firstName;
    private String lastName;
    private String username;

    @Email(message = "email format is invalid")
    private String email;

    private String profilePicture;
    private String phoneNumber;

    private Integer satisfactionScore;
    private Boolean isVip;
}