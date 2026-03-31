package com.example.testaglibrarymanager.feature.category;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.exception.ErrorCode;
import com.example.testaglibrarymanager.feature.category.dto.CategoryDto;
import com.example.testaglibrarymanager.feature.category.dto.CreateCategoryRequest;
import com.example.testaglibrarymanager.feature.category.dto.UpdateCategoryRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Tạo mới thành công danh mục")
    void createCategory_success_returnsSuccessResult() {
        CreateCategoryRequest request = new CreateCategoryRequest("IT");
        Category entity = new Category("IT");
        entity.setId(1L);
        CategoryDto dto = new CategoryDto(1L, "IT");

        when(categoryRepository.existsByName("IT")).thenReturn(false);
        when(categoryMapper.toEntity(request)).thenReturn(new Category("IT"));
        when(categoryRepository.save(any(Category.class))).thenReturn(entity);
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<CategoryDto> result = categoryService.createCategory(request);

        assertTrue(result.isSuccess());
        assertEquals("IT", result.data().name());
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    @DisplayName("Tạo mới thất bại do trùng tên")
    void createCategory_duplicateName_returnsFail() {
        CreateCategoryRequest request = new CreateCategoryRequest("IT");

        when(categoryRepository.existsByName("IT")).thenReturn(true);

        ServiceResult<CategoryDto> result = categoryService.createCategory(request);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.CATEGORY_ALREADY_EXISTS, result.errorCode());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lấy danh mục thành công")
    void getCategoryById_success_returnsSuccessResult() {
        Category entity = new Category("IT");
        entity.setId(1L);
        CategoryDto dto = new CategoryDto(1L, "IT");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<CategoryDto> result = categoryService.getCategoryById(1L);

        assertTrue(result.isSuccess());
        assertEquals("IT", result.data().name());
    }

    @Test
    @DisplayName("Lấy danh mục thất bại do không tìm thấy")
    void getCategoryById_notFound_returnsFail() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        ServiceResult<CategoryDto> result = categoryService.getCategoryById(999L);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, result.errorCode());
    }

    @Test
    @DisplayName("Cập nhật thành công")
    void updateCategory_success_returnsSuccessResult() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("Sci-Fi");
        Category existingCategory = new Category("IT");
        existingCategory.setId(1L);
        Category savedCategory = new Category("Sci-Fi");
        savedCategory.setId(1L);
        CategoryDto dto = new CategoryDto(1L, "Sci-Fi");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Sci-Fi")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toDto(savedCategory)).thenReturn(dto);

        ServiceResult<CategoryDto> result = categoryService.updateCategory(1L, request);

        assertTrue(result.isSuccess());
        assertEquals("Sci-Fi", result.data().name());
    }

    @Test
    @DisplayName("Xóa thành công")
    void deleteCategory_success_returnsSuccessResult() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        ServiceResult<Void> result = categoryService.deleteCategory(1L);

        assertTrue(result.isSuccess());
        verify(categoryRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Xóa thất bại do không tìm thấy")
    void deleteCategory_notFound_returnsFail() {
        when(categoryRepository.existsById(999L)).thenReturn(false);

        ServiceResult<Void> result = categoryService.deleteCategory(999L);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, result.errorCode());
        verify(categoryRepository, never()).deleteById(any());
    }
}
