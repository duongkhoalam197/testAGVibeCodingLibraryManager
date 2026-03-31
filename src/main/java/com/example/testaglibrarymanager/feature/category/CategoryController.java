package com.example.testaglibrarymanager.feature.category;

import com.example.testaglibrarymanager.dto.ApiResponse;
import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.exception.ErrorCode;
import com.example.testaglibrarymanager.feature.category.dto.CategoryDto;
import com.example.testaglibrarymanager.feature.category.dto.CategoryResponse;
import com.example.testaglibrarymanager.feature.category.dto.CreateCategoryRequest;
import com.example.testaglibrarymanager.feature.category.dto.UpdateCategoryRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    public CategoryController(CategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        ServiceResult<List<CategoryDto>> result = categoryService.getAllCategories();
        
        List<CategoryResponse> body = result.data().stream()
                .map(categoryMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable("id") Long id) {
        ServiceResult<CategoryDto> result = categoryService.getCategoryById(id);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        CategoryResponse response = categoryMapper.toResponse(result.data());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        ServiceResult<CategoryDto> result = categoryService.createCategory(request);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        CategoryResponse response = categoryMapper.toResponse(result.data());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        ServiceResult<CategoryDto> result = categoryService.updateCategory(id, request);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        CategoryResponse response = categoryMapper.toResponse(result.data());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable("id") Long id) {
        ServiceResult<Void> result = categoryService.deleteCategory(id);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private <T> ResponseEntity<ApiResponse<T>> mapError(ServiceResult<?> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (result.errorCode() == ErrorCode.CATEGORY_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        } else if (result.errorCode() == ErrorCode.CATEGORY_ALREADY_EXISTS) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(ApiResponse.error(status.value(), result.errorCode().name(), result.message()));
    }
}
