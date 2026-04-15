package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositroy extends JpaRepository<User, Long> {
    Optional<User>findByEmail(String email);
    Optional<User>findById(Long id);
    Optional<User>findByUsername(String username);
    List<User> findByActive(boolean active);
    boolean existsByActive(boolean active);

    @Query("SELECT u FROM User u WHERE u.deleted = false " +
            "AND (:role IS NULL OR u.role = :role) " +
            "AND (:startDate IS NULL OR u.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR u.createdAt <= :endDate)")
    Page<User> findAllWithFilters(
            @Param("role") String role,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );
}
