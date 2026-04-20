package com.alexsysSolutions.alexsis.dto.response.auth;

import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class AuthDtoResponse {
    private String accessToken;
    private String refreshToken;
    private UserDtoResponse user;
}
