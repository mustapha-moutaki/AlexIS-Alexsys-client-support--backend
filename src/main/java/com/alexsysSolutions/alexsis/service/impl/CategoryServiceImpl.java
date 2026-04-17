package com.alexsysSolutions.alexsis.service.impl;

import com.alexsysSolutions.alexsis.dto.request.category.CategoryDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.Category.CategoryDtoResponse;
import com.alexsysSolutions.alexsis.exception.ResourceNotFoundException;
import com.alexsysSolutions.alexsis.exception.ValidationException;
import com.alexsysSolutions.alexsis.mapper.CategoryMapper;
import com.alexsysSolutions.alexsis.model.Category;
import com.alexsysSolutions.alexsis.reposiotry.CategoryRepository;
import com.alexsysSolutions.alexsis.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@Transactional
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDtoResponse create(CategoryDtoRequest dto) {
        //  Added null check and trim for name to ensure data integrity
        logger.info(" Creating new category with name: {}", dto.getName());

        if (!StringUtils.hasText(dto.getName())) {
            logger.error(" Category name cannot be empty");
            throw new ValidationException("Category name cannot be empty.");
        }

        String trimmedName = dto.getName().trim();

        //  Check if category with same name already exists (case-insensitive)
        if(categoryRepository.existsByName(trimmedName)){
            logger.warn(" Category with name '{}' already exists", trimmedName);
            throw new ValidationException(
                    String.format("Category with name '%s' already exists.", trimmedName)
            );
        }

        Category category = categoryMapper.toEntity(dto);
        category.setName(trimmedName);
        /*
        category.setCreatedBy - in the security step
         */
        if (StringUtils.hasText(dto.getDescription())) {
            category.setDescription(dto.getDescription().trim());
        }

        Category savedCategory = categoryRepository.save(category);
        logger.info(" Category created successfully with id: {}", savedCategory.getId());

        return categoryMapper.toDto(savedCategory);
    }

    @Override
    public CategoryDtoResponse update(Long id, CategoryDtoRequest dto) {
        //  Log incoming update request with id
        logger.info(" Updating category with id: {}", id);

        //  Validate id is not null
        if (id == null || id <= 0) {
            logger.error(" Invalid category id: {}", id);
            throw new ValidationException("Category id must be a positive number.");
        }

        //  Check if category exists
        if(!categoryRepository.existsById(id)){
            logger.warn(" Category with id '{}' not found for update", id);
            throw new ResourceNotFoundException(
                    String.format("Category with id '%d' not found.", id)
            );
        }

        //  Validate name is not empty
        if (!StringUtils.hasText(dto.getName())) {
            logger.error(" Category name cannot be empty for update");
            throw new ValidationException("Category name cannot be empty.");
        }

        String trimmedName = dto.getName().trim();

        //  Check if another category with the same name already exists
        if(categoryRepository.existsByName(trimmedName)){
            logger.warn(" Category with name '{}' already exists", trimmedName);
            throw new ValidationException(
                    String.format("Category with name '%s' already exists.", trimmedName)
            );
        }

        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(" category not found")
        );
        category.setName(trimmedName);

        if (StringUtils.hasText(dto.getDescription())) {
            category.setDescription(dto.getDescription().trim());
        } else {
            category.setDescription(null);
        }
        categoryMapper.updateEntity(dto, category);
        Category updatedCategory = categoryRepository.save(category);
        logger.info(" Category with id {} updated successfully", id);

        return categoryMapper.toDto(updatedCategory);
    }

    @Override
    public CategoryDtoResponse getById(Long id) {
        //  Log getById request
        logger.info(" Fetching category with id: {}", id);

        //  Validate id
        if (id == null || id <= 0) {
            logger.error(" Invalid category id: {}", id);
            throw new ValidationException("Category id must be a positive number.");
        }

        //  Check if category exists
        if(!categoryRepository.existsById(id)){
            logger.warn(" Category with id '{}' not found", id);
            throw new ResourceNotFoundException(
                    String.format("Category with id '%d' not found.", id)
            );
        }

        //  Fetch and map category
        Category category = categoryRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("category not found")
        );
        logger.info(" Category with id {} fetched successfully", id);

        return categoryMapper.toDto(category);

    }

    @Override
    public Page<CategoryDtoResponse> getAll(int page, int size, String searchKeyword) {
        //  Log getAll request with pagination and search parameters
        logger.info(" Fetching categories with page: {}, size: {}, searchKeyword: {}", page, size, searchKeyword);

        //  Validate pagination parameters
        if (page < 0 || size <= 0) {
            logger.error(" Invalid pagination parameters - page: {}, size: {}", page, size);
            throw new ValidationException("Page must be >= 0 and size must be > 0.");
        }

        Pageable pageable = PageRequest.of(page, size);

        //  Fetch categories with or without search filter
        Page<Category> categories;

        if (StringUtils.hasText(searchKeyword)) {
            //  Search by category name or description if keyword is provided
            String trimmedKeyword = searchKeyword.trim();
            logger.debug(" Searching categories with keyword: {}", trimmedKeyword);
            categories = categoryRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                    trimmedKeyword, trimmedKeyword, pageable
            );
            logger.info(" Found {} categories matching keyword '{}'", categories.getTotalElements(), trimmedKeyword);
        } else {
            //  Get all categories without search filter
            logger.debug(" Fetching all categories without search filter");
            categories = categoryRepository.findAll(pageable);
            logger.info(" Found {} total categories", categories.getTotalElements());
        }

        //  Map entities to DTOs and return paginated result
        Page<CategoryDtoResponse> result = categories.map(categoryMapper::toDto);
        logger.info(" Returning page {} with {} categories", page, result.getNumberOfElements());

        return result;
    }

    @Override
    public void delete(Long id) {
        //  Log delete request
        logger.info(" Deleting category with id: {}", id);

        //  Validate id
        if (id == null || id <= 0) {
            logger.error(" Invalid category id for deletion: {}", id);
            throw new ValidationException("Category id must be a positive number.");
        }

        //  Check if category exists before deletion
        if (!categoryRepository.existsById(id)) {
            logger.warn(" Category with id '{}' not found for deletion", id);
            throw new ResourceNotFoundException(
                    String.format("Category with id '%d' not found.", id)
            );
        }

        //  Delete category from database
        try {
            categoryRepository.deleteById(id);
            logger.info(" Category with id {} deleted successfully", id);
        } catch (Exception e) {
            logger.error(" Error occurred while deleting category with id: {}", id, e);
            throw new ValidationException("Error occurred while deleting category: " + e.getMessage());
        }
    }
}
