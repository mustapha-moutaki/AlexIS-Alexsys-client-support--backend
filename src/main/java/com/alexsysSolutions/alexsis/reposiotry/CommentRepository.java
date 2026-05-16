package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    // st: find all comments by ticket id
    Page<Comment> findByTicketId(Long ticketId, Pageable pageable);

    @Query("""
    SELECT c
    FROM Comment c
    JOIN FETCH c.author
    WHERE c.id = :id
""")
    Optional<Comment> findByIdWithAuthor(Long id);
}
