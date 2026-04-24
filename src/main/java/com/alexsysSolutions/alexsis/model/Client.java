package com.alexsysSolutions.alexsis.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "id")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Client extends User {

    @Column(name = "registration_date", nullable = false)
    private LocalDateTime registrationDate;

    @Column(name = "satisfaction_score")
    private Integer satisfactionScore;

    @Column(name = "last_interaction_at")
    private LocalDateTime lastInteractAt;

    @Column(name = "is_vip")
    private Boolean isVip;
}