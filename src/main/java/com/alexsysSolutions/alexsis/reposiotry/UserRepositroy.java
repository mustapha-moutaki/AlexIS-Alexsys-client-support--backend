package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositroy extends JpaRepository<User, Long> {
    Optional<User>findByEmail(String email);
    Optional<User>findById(Long id);
    List<User> findByActive(boolean active);
    boolean existsByActive(boolean active);
}
