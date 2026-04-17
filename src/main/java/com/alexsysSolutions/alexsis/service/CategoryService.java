package com.alexsysSolutions.alexsis.service;


import com.alexsysSolutions.alexsis.dto.request.category.CategoryDtoRequest;
import com.alexsysSolutions.alexsis.dto.response.Category.CategoryDtoResponse;
import org.springframework.data.domain.Page;

public interface CategoryService {


    CategoryDtoResponse create(CategoryDtoRequest dto);
    CategoryDtoResponse update(Long id, CategoryDtoRequest dto);
    CategoryDtoResponse getById(Long id);
    Page<CategoryDtoResponse> getAll(int page, int size, String searchKeyword);
    void delete(Long id);
}
