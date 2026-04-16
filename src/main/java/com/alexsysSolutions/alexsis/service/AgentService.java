package com.alexsysSolutions.alexsis.service;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.agent.AgentDtoResponse;
import org.springframework.data.domain.Page;

public interface AgentService {
    AgentDtoResponse create(AgentCreateDtoRequest dto);
    AgentDtoResponse update(Long id, AgentUpdateDtoRequest dto);
    Page<AgentDtoResponse>getAll(int page, int size);
    AgentDtoResponse getById(Long id);
    void delete(Long id);
}
