package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.dto.CategoryDto;
import com.example.testaglibrarymanager.model.response.CategoryResponse;
import com.example.testaglibrarymanager.model.request.CreateCategoryRequest;
import com.example.testaglibrarymanager.model.entity.Category;


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

