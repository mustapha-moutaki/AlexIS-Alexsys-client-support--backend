package com.alexsysSolutions.alexsis.service.impl.dashboardServiceImpl;

import com.alexsysSolutions.alexsis.reposiotry.CategoryRepository;
import com.alexsysSolutions.alexsis.service.CategoryService;
import com.alexsysSolutions.alexsis.service.dashboardService.CategoryStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryStatsServiceImpl implements CategoryStatsService {
    private final CategoryRepository categoryRepository;

    @Override
    public int totalCategories() {
        return categoryRepository.totalCategories();
    }
}
