package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.user.UserCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.user.UserUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.user.UserDtoResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface UserService {
    UserDtoResponse create(UserCreateDtoRequest dto);
    UserDtoResponse update (Long id, UserUpdateDtoRequest dto);
    UserDtoResponse getById(Long id);
    void delete(Long id);

    Page<UserDtoResponse>getAllIncludingDeleted(int page, int size);
    Page<UserDtoResponse> getAll(
            int page,
            int size,
            String sortBy,
            String direction,
            String role,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
