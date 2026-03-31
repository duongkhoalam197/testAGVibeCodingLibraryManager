package com.example.testaglibrarymanager.service.category;

import com.example.testaglibrarymanager.model.dto.CategoryDto;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.request.CreateCategoryRequest;
import com.example.testaglibrarymanager.model.request.UpdateCategoryRequest;



import java.util.List;

public interface CategoryService {
    ServiceResult<CategoryDto> createCategory(CreateCategoryRequest request);
    ServiceResult<CategoryDto> getCategoryById(Long id);
    ServiceResult<List<CategoryDto>> getAllCategories();
    ServiceResult<CategoryDto> updateCategory(Long id, UpdateCategoryRequest request);
    ServiceResult<Void> deleteCategory(Long id);
}

