package com.alexsysSolutions.alexsis.reposiotry;

import com.alexsysSolutions.alexsis.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
        boolean existsById(Long id);
        boolean existsByName(String name);

        Page<Category> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                String name, String description, Pageable pageable
        );

        Category findByName(String name);
}
