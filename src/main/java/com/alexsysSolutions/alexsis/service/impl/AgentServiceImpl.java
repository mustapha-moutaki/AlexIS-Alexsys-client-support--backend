package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.agent.AgentCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.agent.AgentUpdateDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.agent.AgentDtoResponse;
import com.alexsysSolutions.alexsis.enums.AvailabilityStatus;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.AgentMapper;
import com.alexsysSolutions.alexsis.mapper.UserMapper;
import com.alexsysSolutions.alexsis.model.Agent;
import com.alexsysSolutions.alexsis.reposiotry.AgentRepository;
import com.alexsysSolutions.alexsis.service.AgentService;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AgentRepository agentRepository;
    private final AgentMapper agentMapper;
    private final UserMapper userMapper;
    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    @Override
    public AgentDtoResponse create(AgentCreateDtoRequest dto) {
        logger.info(" Creating new agent with email: {}", dto.getEmail());

        if(agentRepository.findByEmail(dto.getEmail()).isPresent()){
            logger.warn("Agent with email already exists: {}", dto.getEmail());
            throw new ValidationException(
                    String.format("Agent with this email: %s already exists", dto.getEmail())
            );
        }
        if(agentRepository.findByUsername(dto.getUsername()).isPresent()){
            logger.warn("Agent with username already exists: {}", dto.getUsername());
            throw new ValidationException(
                    String.format("Agent with this username: %s already exists", dto.getUsername())
            );
        }

        // Mapping and Saving
        Agent agent = agentMapper.toEntity(dto);
        agent.setPassword(PasswordUtil.hash(dto.getPassword()));
        agent.setRole(UserRole.AGENT);
        agent.setAvailabilityStatus(AvailabilityStatus.NOT_SELECTED);
        /*
            createdBy in the security part
         */
        Agent savedAgent = agentRepository.save(agent);
        logger.info("Agent created successfully with id: {}", savedAgent.getId());
        return agentMapper.toDto(savedAgent);
    }

    @Override
    public AgentDtoResponse update(Long id, AgentUpdateDtoRequest dto) {
        logger.info("Updating agent with id: {}", id);
        Agent agent = agentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Agent not found with id: {}", id);
                    return new ValidationException(
                            String.format("Agent with id: %d not found", id)
                    );
                });

        if (dto.getEmail() != null && !dto.getEmail().equals(agent.getEmail())) {

            boolean emailExists = agentRepository.findByEmail(dto.getEmail()).isPresent();

            if (emailExists) {
                logger.warn("Email already in use: {}", dto.getEmail());
                throw new ValidationException(
                        String.format("Email %s is already used by another user", dto.getEmail())
                );
            }
            agent.setEmail(dto.getEmail());
        }

        if(dto.getPassword() != null){
            agent.setPassword(PasswordUtil.hash(dto.getPassword()));
            logger.debug("Agent password updated");
        }
        agentMapper.updateEntity(dto, agent);
        Agent savedAgent = agentRepository.save(agent);
        logger.info("Agent updated successfully with id: {}", id);
        return agentMapper.toDto(savedAgent);

    }

    @Override
    public Page<AgentDtoResponse> getAll(int page, int size) {
        logger.info("Fetching all agents - page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<AgentDtoResponse> result = agentRepository.findAll(pageable)
                .map(agentMapper::toDto);  // Changed lambda to method reference (cleaner)
        logger.info("Retrieved {} agents", result.getTotalElements());
        return result;
    }

    @Override
    public AgentDtoResponse getById(Long id) {
        logger.info("Fetching agent with id: {}", id);
        Agent agent = agentRepository.findById(id).orElseThrow(
                ()-> {
                    logger.error("Agent not found with id: {}", id);
                    return new ResourceNotFoundException("agent not found ");
                }
        );
        return agentMapper.toDto(agent);
    }

    @Override
    public void delete(Long id) {
        logger.info("Deleting agent with id: {}", id);
        Agent agent = agentRepository.findById(id).orElseThrow(
                ()-> {
                    logger.error("Agent not found with id: {}", id);
                    return new ResourceNotFoundException("agent not found ");
                }
        );
        agentRepository.delete(agent);
        logger.info("Agent deleted successfully with id: {}", id);
    }
}
