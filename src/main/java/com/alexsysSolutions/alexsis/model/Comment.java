package com.alexsysSolutions.alexsis.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comments")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment extends BaseEntity{

    private String content;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
}
