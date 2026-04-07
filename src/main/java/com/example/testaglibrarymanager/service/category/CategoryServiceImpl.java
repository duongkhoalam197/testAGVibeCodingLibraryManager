package com.example.testaglibrarymanager.service.category;

import com.example.testaglibrarymanager.model.dto.CategoryDto;
import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.mapper.CategoryMapper;
import com.example.testaglibrarymanager.messaging.CategoryEventProducer;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.request.CreateCategoryRequest;
import com.example.testaglibrarymanager.model.request.UpdateCategoryRequest;
import com.example.testaglibrarymanager.repository.CategoryRepository;
import com.example.testaglibrarymanager.util.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

import com.example.testaglibrarymanager.model.entity.Category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final CategoryEventProducer categoryEventProducer;

    @Override
    @Transactional
    public ServiceResult<CategoryDto> createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            return ServiceResult.fail(ErrorCode.CATEGORY_ALREADY_EXISTS, "Danh mục đã tồn tại: " + request.name());
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);

        CategoryDto eventCategoryDto = categoryMapper.toEventDto(savedCategory, KafkaConfig.CATEGORY_CREATE_TOPIC);
        categoryEventProducer.publishCategoryEvent(KafkaConfig.CATEGORY_CREATE_TOPIC, eventCategoryDto);
        return ServiceResult.success(categoryMapper.toDto(savedCategory));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> ServiceResult.success(categoryMapper.toDto(category)))
                .orElseGet(() -> ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND,
                        "Không tìm thấy danh mục với id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryRepository.findAll().stream()
                .map(categoryMapper::toDto)
                .collect(Collectors.toList());
        return ServiceResult.success(categories);
    }

    @Override
    @Transactional
    public ServiceResult<CategoryDto> updateCategory(Long id, UpdateCategoryRequest request) {
        return categoryRepository.findById(id).map(category -> {
            // Check for name collision with ANOTHER category
            if (!category.getName().equals(request.name()) && categoryRepository.existsByName(request.name())) {
                return ServiceResult.<CategoryDto>fail(ErrorCode.CATEGORY_ALREADY_EXISTS,
                        "Danh mục đã tồn tại: " + request.name());
            }

            category.setName(request.name());
            Category updatedCategory = categoryRepository.save(category);

            CategoryDto eventCategoryDto = categoryMapper.toEventDto(updatedCategory,
                    KafkaConfig.CATEGORY_UPDATED_TOPIC);
            categoryEventProducer.publishCategoryEvent(KafkaConfig.CATEGORY_UPDATED_TOPIC, eventCategoryDto);

            return ServiceResult.success(categoryMapper.toDto(updatedCategory));
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với id: " + id));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteCategory(Long id) {
        return categoryRepository.findById(id).map(category -> {
            CategoryDto deleteCategoryEvent = categoryMapper.toEventDto(category,
                    KafkaConfig.CATEGORY_DELETE_TOPIC);
            categoryEventProducer.publishCategoryEvent(KafkaConfig.CATEGORY_DELETE_TOPIC, deleteCategoryEvent);

            categoryRepository.delete(category);
            return ServiceResult.<Void>success(null);
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với id: " + id));
    }
}
