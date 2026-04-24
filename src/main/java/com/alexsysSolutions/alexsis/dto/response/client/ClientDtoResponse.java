package com.alexsysSolutions.alexsis.dto.response.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ClientDtoResponse {

    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String phoneNumber;
    private String profilePicture;
    private boolean active;
    private boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;

    private LocalDateTime registrationDate;
    private Integer satisfactionScore;
    private LocalDateTime lastInteractAt;
    private boolean isVip;
}