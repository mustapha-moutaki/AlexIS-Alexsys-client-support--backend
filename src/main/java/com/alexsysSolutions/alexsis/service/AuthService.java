package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.auth.LoginDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.auth.AuthDtoResponse;

public interface AuthService {
    AuthDtoResponse login(LoginDtoRequest dto);
    AuthDtoResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
}
