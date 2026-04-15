package com.alexsysSolutions.alexsis.dto.response.user;

import com.alexsysSolutions.alexsis.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDtoResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String username;
    private String profilePicture;
    private String role;

}

