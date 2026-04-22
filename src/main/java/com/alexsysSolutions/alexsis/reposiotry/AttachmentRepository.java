package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.enums.AttachmentStatus;
import com.alexsysSolutions.alexsis.model.Attachment;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Page<Attachment> findAllByTicket_Id(Long ticketId, Pageable pageable);
    boolean existsById(@NonNull Long id);

    List<Attachment> findAllByIdIn(List<Long> ids);
    List<Attachment> findByTicketIsNullAndStatus(AttachmentStatus status);
}
