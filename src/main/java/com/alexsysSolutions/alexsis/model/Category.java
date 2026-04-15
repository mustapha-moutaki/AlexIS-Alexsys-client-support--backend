package com.alexsysSolutions.alexsis.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categories")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Category extends BaseEntity{

    private String name;
    private String description;

    // we don't need to link betweeen category and list of ticket because it's over design

}
