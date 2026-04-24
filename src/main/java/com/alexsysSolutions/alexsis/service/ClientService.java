package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.Client.ClientCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateProfileDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.client.ClientDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.client.ClientUpdateProfileDtoResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface ClientService {
    ClientDtoResponse create(ClientCreateDtoRequest dto);
    ClientDtoResponse updateByAdmin(Long clientId, ClientUpdateByAdminDtoRequest dto);
    ClientUpdateProfileDtoResponse updateProfile(Long clientId, ClientUpdateProfileDtoRequest dto);

    ClientDtoResponse getById(Long clientId);
    Page<ClientDtoResponse> getAll(
            int page,
            int size,
            Boolean isVip,
            Boolean isActive,
            String sortDirection);

    void delete(Long clientId);
}