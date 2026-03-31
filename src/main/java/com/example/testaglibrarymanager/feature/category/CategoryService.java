package com.example.testaglibrarymanager.feature.category;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.feature.category.dto.CategoryDto;
import com.example.testaglibrarymanager.feature.category.dto.CreateCategoryRequest;
import com.example.testaglibrarymanager.feature.category.dto.UpdateCategoryRequest;

import java.util.List;

public interface CategoryService {
    ServiceResult<CategoryDto> createCategory(CreateCategoryRequest request);
    ServiceResult<CategoryDto> getCategoryById(Long id);
    ServiceResult<List<CategoryDto>> getAllCategories();
    ServiceResult<CategoryDto> updateCategory(Long id, UpdateCategoryRequest request);
    ServiceResult<Void> deleteCategory(Long id);
}
