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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private CurrentUserProvider currentUser;

    @InjectMocks
    private ClientServiceImpl clientService;



    @Test
    void shouldCreateClientSuccessfully() {

        ClientCreateDtoRequest request = ClientCreateDtoRequest.builder()
                .firstName("mustapha")
                .lastName("moutaki")
                .email("test@gmail.com")
                .username("mustapha")
                .password("password123")
                .build();

        when(clientRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.empty());

        when(clientRepository.findByUsername(request.getUsername()))
                .thenReturn(Optional.empty());


        Client savedClient = new Client();
        savedClient.setId(1L);

        when(clientRepository.save(any(Client.class)))
                .thenReturn(savedClient);

        when(clientMapper.toDto(any(Client.class)))
                .thenReturn(new ClientDtoResponse());

        ClientDtoResponse result = clientService.create(request);

        assertNotNull(result);

        verify(clientRepository).findByEmail(request.getEmail());
        verify(clientRepository).findByUsername(request.getUsername());
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void updateByAdmin() {

        // ARRANGE
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setFirstName("mustapha");
        client.setLastName("moutaki");
        client.setEmail("old@mail.com");
        client.setUsername("oldUser");

        ClientUpdateByAdminDtoRequest dto = new ClientUpdateByAdminDtoRequest();
        dto.setEmail("newEmailInsertedByAdmin@gmail.com");
        dto.setFirstName("mustaphaUpdatedByAdmin");
        dto.setLastName("moutakiUpdatedByAdmin");
        dto.setUsername("newUser");

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(clientRepository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.empty());

        when(clientRepository.findByUsername(dto.getUsername()))
                .thenReturn(Optional.empty());

        when(clientRepository.save(any(Client.class)))
                .thenReturn(client);

        when(clientMapper.toDto(any(Client.class)))
                .thenReturn(new ClientDtoResponse());

        // ACT
        var result = clientService.updateByAdmin(clientId, dto);

        // ASSERT
        assertNotNull(result);

        assertEquals("mustaphaUpdatedByAdmin", client.getFirstName());
        assertEquals("moutakiUpdatedByAdmin", client.getLastName());
        assertEquals("newEmailInsertedByAdmin@gmail.com", client.getEmail());

        verify(clientRepository).findById(clientId);
        verify(clientRepository).findByEmail(dto.getEmail());
        verify(clientRepository).findByUsername(dto.getUsername());
        verify(clientRepository).save(client);
        verify(clientMapper).toDto(client);
    }

    @Test
    void updateProfile() {
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        ClientUpdateProfileDtoRequest dto = new ClientUpdateProfileDtoRequest();
        dto.setFirstName("mustapha");
        dto.setLastName("moutaki");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        var result = clientService.updateProfile(clientId, dto);
        assertEquals("mustapha", client.getFirstName());
        assertEquals("moutaki", client.getLastName());
        assertNotNull(result);
        verify(clientRepository).findById(clientId);
        verify(clientRepository).save(client);
    }

    @Test
    void shouldReturnClientById() {
        // arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setId(clientId);

        ClientDtoResponse dto = new ClientDtoResponse();
        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.toDto(client)).thenReturn(dto);

        var result = clientService.getById(clientId);

        assertNotNull(result);
        verify(clientRepository).findById(clientId);
        verify(clientMapper).toDto(client);
    }

    @Test
    void getAll() {
    }

    @Test
    void shouldDeleteClientSuccessfully() {

        // ARRANGE
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        // ACT
        clientService.delete(clientId);

        // ASSERT
        verify(clientRepository).delete(client);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        // Arrange
        Long clientId =1L;
        Client client = new Client();
        client.setId(clientId);
        client.setPassword("hashedOldPassword");

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(passwordEncoder.matches("oldPass", "hashedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPass")).thenReturn("hashedNewPassword");

        // act
        clientService.changePassword(clientId, "oldPass", "newPass");

        // assert
        assertEquals("hashedNewPassword", client.getPassword());
        Mockito.verify(clientRepository).save(client);
    }

    @Test
    void shouldThrowExceptionWhenCurrentPasswordIsWrong() {

        // ARRANGE
        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setPassword("hashedOldPassword");

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(passwordEncoder.matches("wrongPass", "hashedOldPassword"))
                .thenReturn(false);

        // ACT + ASSERT
        assertThrows(ValidationException.class, () ->
                clientService.changePassword(clientId, "wrongPass", "newPass")
        );

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenNewPasswordIsSameAsOld() {

        Long clientId = 1L;

        Client client = new Client();
        client.setId(clientId);
        client.setPassword("hashedOldPassword");

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.of(client));

        when(passwordEncoder.matches("oldPass", "hashedOldPassword"))
                .thenReturn(true);

        when(passwordEncoder.matches("newPass", "hashedOldPassword"))
                .thenReturn(true); // same password detected

        assertThrows(ValidationException.class, () ->
                clientService.changePassword(clientId, "oldPass", "newPass")
        );

        verify(clientRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenClientNotFound() {

        Long clientId = 1L;

        when(clientRepository.findById(clientId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                clientService.changePassword(clientId, "oldPass", "newPass")
        );

        verifyNoInteractions(passwordEncoder);
        verify(clientRepository, never()).save(any());
    }
}