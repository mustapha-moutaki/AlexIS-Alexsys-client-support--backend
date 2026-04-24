package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.Client.ClientCreateDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateByAdminDtoRequest;
import com.alexsysSolutions.alexsis.dto.request.Client.ClientUpdateProfileDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.client.ClientDtoResponse;
import com.alexsysSolutions.alexsis.dto.response.client.ClientUpdateProfileDtoResponse;
import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.ClientMapper;
import com.alexsysSolutions.alexsis.model.Client;
import com.alexsysSolutions.alexsis.reposiotry.ClientRepository;
import com.alexsysSolutions.alexsis.security.context.CurrentUserProvider;
import com.alexsysSolutions.alexsis.service.ClientService;
import com.alexsysSolutions.alexsis.util.PasswordUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final CurrentUserProvider currentUser;
    private static final Logger logger = LoggerFactory.getLogger(ClientServiceImpl.class);

    @Override
    public ClientDtoResponse create(ClientCreateDtoRequest dto) {
        logger.info("Creating new client with email: {}", dto.getEmail());

        if (clientRepository.findByEmail(dto.getEmail()).isPresent()) {
            logger.warn("Client with email already exists: {}", dto.getEmail());
            throw new ValidationException("Client with this email already exists: " + dto.getEmail());
        }
        if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
            logger.warn("Client with username already exists: {}", dto.getUsername());
            throw new ValidationException("Client with this username already exists: " + dto.getUsername());
        }

        // Manually create and populate Client
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setLastName(dto.getLastName());
        client.setUsername(dto.getUsername());
        client.setEmail(dto.getEmail());
        client.setPassword(PasswordUtil.hash(dto.getPassword()));
        client.setProfilePicture(dto.getProfilePicture());
        client.setPhoneNumber(dto.getPhoneNumber());
        client.setRole(UserRole.CLIENT);
        client.setRegistrationDate(LocalDateTime.now());
        client.setActive(true);
        client.setDeleted(false);
        client.setCreatedBy(currentUser.getEmail());

        Client savedClient = clientRepository.save(client);
        logger.info("Client created successfully with ID: {}", savedClient.getId());
        return clientMapper.toDto(savedClient);
    }


    @Override
    public ClientDtoResponse updateByAdmin(Long clientId, ClientUpdateByAdminDtoRequest dto) {
        logger.info("Updating client with ID: {} by admin", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", clientId);
                    return new ResourceNotFoundException("Client not found with ID: " + clientId);
                });

        // Handle email and username separately for uniqueness
        if (dto.getEmail() != null && !dto.getEmail().equals(client.getEmail())) {
            if (clientRepository.findByEmail(dto.getEmail()).isPresent()) {
                logger.warn("Email already in use: {}", dto.getEmail());
                throw new ValidationException("Email is already in use: " + dto.getEmail());
            }
            client.setEmail(dto.getEmail());
        }

        if (dto.getUsername() != null && !dto.getUsername().equals(client.getUsername())) {
            if (clientRepository.findByUsername(dto.getUsername()).isPresent()) {
                logger.warn("Username already in use: {}", dto.getUsername());
                throw new ValidationException("Username is already in use: " + dto.getUsername());
            }
            client.setUsername(dto.getUsername());
        }

        // Manually update other fields (only if not null)
        if (dto.getFirstName() != null) client.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) client.setLastName(dto.getLastName());
        if (dto.getProfilePicture() != null) client.setProfilePicture(dto.getProfilePicture());
        if (dto.getPhoneNumber() != null) client.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getSatisfactionScore() != null) client.setSatisfactionScore(dto.getSatisfactionScore());
        if (dto.getIsVip() != null) client.setIsVip(dto.getIsVip());

        Client savedClient = clientRepository.save(client);
        logger.info("Client updated successfully with ID: {}", clientId);
        return clientMapper.toDto(savedClient);
    }

    @Override
    public ClientUpdateProfileDtoResponse updateProfile(Long clientId, ClientUpdateProfileDtoRequest dto) {
        logger.info("Updating profile for client ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", clientId);
                    return new ResourceNotFoundException("Client not found with ID: " + clientId);
                });

        // Manually update profile fields (only if not null)
        if (dto.getFirstName() != null) client.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) client.setLastName(dto.getLastName());
        if (dto.getPhoneNumber() != null) client.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getProfilePicture() != null) client.setProfilePicture(dto.getProfilePicture());

        Client savedClient = clientRepository.save(client);

        // Return only the updated profile fields
        ClientUpdateProfileDtoResponse response = new ClientUpdateProfileDtoResponse();
        response.setFirstName(savedClient.getFirstName());
        response.setLastName(savedClient.getLastName());
        response.setPhoneNumber(savedClient.getPhoneNumber());
        response.setProfilePicture(savedClient.getProfilePicture());

        logger.info("Profile updated successfully for client ID: {}", clientId);
        return response;
    }

    @Override
    public ClientDtoResponse getById(Long clientId) {
        logger.info("Fetching client with ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", clientId);
                    return new ResourceNotFoundException("Client not found with ID: " + clientId);
                });

        logger.info("Client retrieved successfully with ID: {}", clientId);
        return clientMapper.toDto(client);
    }

    @Override
    public Page<ClientDtoResponse> getAll(
            int page,
            int size,
            Boolean isVip,
            Boolean isActive,
            String sortDirection
    ) {
        logger.info("Fetching all clients - page: {}, size: {}, isVip: {}, isActive: {}", page, size, isVip, isActive);

        Sort sort = Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("lastInteractAt")
        );

        if ("asc".equalsIgnoreCase(sortDirection)) {
            sort = Sort.by(
                    Sort.Order.asc("createdAt"),
                    Sort.Order.asc("lastInteractAt")
            );
        }

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClientDtoResponse> result = clientRepository.findAllWithFilters(isVip, isActive, pageable)
                .map(clientMapper::toDto);

        logger.info("Retrieved {} clients", result.getTotalElements());
        return result;
    }

    @Override
    public void delete(Long clientId) {
        logger.info("Deleting client with ID: {}", clientId);

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> {
                    logger.error("Client not found with ID: {}", clientId);
                    return new ResourceNotFoundException("Client not found with ID: " + clientId);
                });

        clientRepository.delete(client);
        logger.info("Client deleted successfully with ID: {}", clientId);
    }
}