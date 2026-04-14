package com.alexsysSolutions.alexsis.model;

import com.alexsysSolutions.alexsis.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User extends BaseEntity {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 6)
    private String password;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "profile_picture", nullable = true)
    private String profilePicture;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "is_deleted")
    private boolean deleted = false;

    // id and update_at and created_at are inhiretd form baseEntity

}
