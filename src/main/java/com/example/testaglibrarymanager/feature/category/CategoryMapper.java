package com.example.testaglibrarymanager.feature.category;

import com.example.testaglibrarymanager.feature.category.dto.CategoryDto;
import com.example.testaglibrarymanager.feature.category.dto.CategoryResponse;
import com.example.testaglibrarymanager.feature.category.dto.CreateCategoryRequest;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request) {
        if (request == null) {
            return null;
        }
        return new Category(request.name());
    }

    public CategoryDto toDto(Category entity) {
        if (entity == null) {
            return null;
        }
        return new CategoryDto(entity.getId(), entity.getName());
    }

    public CategoryResponse toResponse(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        return new CategoryResponse(dto.id(), dto.name());
    }
}
