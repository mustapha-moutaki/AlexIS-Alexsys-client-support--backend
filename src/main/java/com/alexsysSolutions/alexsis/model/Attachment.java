package com.alexsysSolutions.alexsis.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "file_size", nullable = true)
    private Long fileSize;

    private LocalDateTime uploadedAt;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @PrePersist
    private void onCreate(){
        this.uploadedAt = LocalDateTime.now();
    }

}
