package com.alexsysSolutions.alexsis.dto.request.Client;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientCreateDtoRequest {
    @NotBlank(message = "first name is required")
    private String firstName;
    @NotBlank(message = "last name is required")
    private String lastName;
    @NotBlank(message = "username is required")
    private String username;

    @NotBlank(message = "email is required")
    @Email(message = "email format is invalid")
    private String email;
    @NotBlank(message = "password is required")
    @Size(min = 6, message = "the password must be at least 6 characters")
    private String password;

    private String profilePicture;
    private String phoneNumber;
}