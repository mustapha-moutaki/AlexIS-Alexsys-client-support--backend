package com.alexsysSolutions.alexsis.dto.request.user;

import com.alexsysSolutions.alexsis.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserUpdateDtoRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    private String phoneNumber;
    private String profilePicture;
    private UserRole role;
}
