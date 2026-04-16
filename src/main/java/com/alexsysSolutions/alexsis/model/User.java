package com.alexsysSolutions.alexsis.model;

import com.alexsysSolutions.alexsis.enums.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)

// This tells Hibernate: when someone calls repository.delete(), run this SQL instead
@SQLDelete(sql = "UPDATE users SET is_deleted = true, is_active = false WHERE id = ?")

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

    @Builder.Default
    @Column(name = "is_active")
    private boolean active = true;

    @Builder.Default
    @Column(name = "is_deleted")
    private boolean deleted = false;

    // id and update_at and created_at are inhiretd form baseEntity

}
