package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.dto.CategoryDto;
import com.example.testaglibrarymanager.model.response.CategoryResponse;
import com.example.testaglibrarymanager.model.request.CreateCategoryRequest;
import com.example.testaglibrarymanager.model.entity.Category;

import java.time.LocalDateTime;

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
        return new CategoryDto(null, entity.getId(), entity.getName(), null);
    }

    public CategoryDto toEventDto(Category entity, String eventType) {
        if (entity == null) {
            return null;
        }
        return new CategoryDto(
                eventType,
                entity.getId(),
                entity.getName(),
                LocalDateTime.now());
    }

    public CategoryResponse toResponse(CategoryDto dto) {
        if (dto == null) {
            return null;
        }
        return new CategoryResponse(dto.id(), dto.name());
    }
}
