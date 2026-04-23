package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.enums.UserRole;
import com.alexsysSolutions.alexsis.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE " +
            "(:includeDeleted = true OR u.deleted = false) " + // Logic to show/hide deleted
            "AND (:role IS NULL OR u.role = :role) " +
            "AND (cast(:startDate as timestamp) IS NULL OR u.createdAt >= :startDate) " +
            "AND (cast(:endDate as timestamp) IS NULL OR u.createdAt <= :endDate)")
    Page<User> findAllWithFilters(
            @Param("role") UserRole role,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("includeDeleted") boolean includeDeleted,
            Pageable pageable
    );

    Optional<User>findByIdAndRole(Long id, UserRole role);
    boolean existsByRole(UserRole role);
}