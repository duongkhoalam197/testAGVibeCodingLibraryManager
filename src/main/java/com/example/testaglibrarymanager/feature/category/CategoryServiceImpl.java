package com.example.testaglibrarymanager.feature.category;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.exception.ErrorCode;
import com.example.testaglibrarymanager.feature.category.dto.CategoryDto;
import com.example.testaglibrarymanager.feature.category.dto.CreateCategoryRequest;
import com.example.testaglibrarymanager.feature.category.dto.UpdateCategoryRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public ServiceResult<CategoryDto> createCategory(CreateCategoryRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            return ServiceResult.fail(ErrorCode.CATEGORY_ALREADY_EXISTS, "Danh mục đã tồn tại: " + request.name());
        }

        Category category = categoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return ServiceResult.success(categoryMapper.toDto(savedCategory));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<CategoryDto> getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .map(category -> ServiceResult.success(categoryMapper.toDto(category)))
                .orElseGet(() -> ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với id: " + id));
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
                return ServiceResult.<CategoryDto>fail(ErrorCode.CATEGORY_ALREADY_EXISTS, "Danh mục đã tồn tại: " + request.name());
            }

            category.setName(request.name());
            Category updatedCategory = categoryRepository.save(category);
            return ServiceResult.success(categoryMapper.toDto(updatedCategory));
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với id: " + id));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            return ServiceResult.fail(ErrorCode.CATEGORY_NOT_FOUND, "Không tìm thấy danh mục với id: " + id);
        }
        categoryRepository.deleteById(id);
        return ServiceResult.success(null);
    }
}
