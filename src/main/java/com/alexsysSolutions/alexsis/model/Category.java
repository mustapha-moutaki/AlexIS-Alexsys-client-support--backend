package com.alexsysSolutions.alexsis.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder

public class Category extends BaseEntity{

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // we don't need to link betweeen category and list of ticket because it's over design

}
