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
import com.example.testaglibrarymanager.model.entity.Category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CategoryEventProducer categoryEventProducer;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Tạo mới thành công danh mục")
    void createCategory_success_returnsSuccessResult() {
        CreateCategoryRequest request = new CreateCategoryRequest("IT");
        Category entity = new Category("IT");
        entity.setId(1L);
        CategoryDto dto = new CategoryDto(null, 1L, "IT", null);
        CategoryDto eventDto = new CategoryDto(KafkaConfig.CATEGORY_CREATE_TOPIC, 1L, "IT", LocalDateTime.now());

        when(categoryRepository.existsByName("IT")).thenReturn(false);
        when(categoryMapper.toEntity(request)).thenReturn(new Category("IT"));
        when(categoryRepository.save(any(Category.class))).thenReturn(entity);
        when(categoryMapper.toEventDto(any(Category.class), anyString())).thenReturn(eventDto);
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<CategoryDto> result = categoryService.createCategory(request);

        assertTrue(result.isSuccess());
        assertEquals("IT", result.data().name());
        verify(categoryRepository, times(1)).save(any(Category.class));
        verify(categoryEventProducer, times(1)).publishCategoryEvent(anyString(), any(CategoryDto.class));
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
        CategoryDto dto = new CategoryDto(null, 1L, "IT", null);

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
    @DisplayName("Lấy tất cả danh mục thành công")
    void getAllCategories_success_returnsList() {
        Category entity = new Category("IT");
        CategoryDto dto = new CategoryDto(null, 1L, "IT", null);
        
        when(categoryRepository.findAll()).thenReturn(List.of(entity));
        when(categoryMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<List<CategoryDto>> result = categoryService.getAllCategories();

        assertTrue(result.isSuccess());
        assertFalse(result.data().isEmpty());
        assertEquals(1, result.data().size());
    }

    @Test
    @DisplayName("Cập nhật thành công")
    void updateCategory_success_returnsSuccessResult() {
        UpdateCategoryRequest request = new UpdateCategoryRequest("Sci-Fi");
        Category existingCategory = new Category("IT");
        existingCategory.setId(1L);
        Category savedCategory = new Category("Sci-Fi");
        savedCategory.setId(1L);
        CategoryDto dto = new CategoryDto(null, 1L, "Sci-Fi", null);
        CategoryDto eventDto = new CategoryDto(KafkaConfig.CATEGORY_UPDATED_TOPIC, 1L, "Sci-Fi", LocalDateTime.now());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.existsByName("Sci-Fi")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.toEventDto(any(Category.class), anyString())).thenReturn(eventDto);
        when(categoryMapper.toDto(savedCategory)).thenReturn(dto);

        ServiceResult<CategoryDto> result = categoryService.updateCategory(1L, request);

        assertTrue(result.isSuccess());
        assertEquals("Sci-Fi", result.data().name());
        verify(categoryEventProducer, times(1)).publishCategoryEvent(anyString(), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Xóa thành công")
    void deleteCategory_success_returnsSuccessResult() {
        Category entity = new Category("IT");
        entity.setId(1L);
        CategoryDto eventDto = new CategoryDto(KafkaConfig.CATEGORY_DELETE_TOPIC, 1L, "IT", LocalDateTime.now());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(categoryMapper.toEventDto(any(Category.class), anyString())).thenReturn(eventDto);

        ServiceResult<Void> result = categoryService.deleteCategory(1L);

        assertTrue(result.isSuccess());
        verify(categoryRepository, times(1)).delete(any(Category.class));
        verify(categoryEventProducer, times(1)).publishCategoryEvent(anyString(), any(CategoryDto.class));
    }

    @Test
    @DisplayName("Xóa thất bại do không tìm thấy")
    void deleteCategory_notFound_returnsFail() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        ServiceResult<Void> result = categoryService.deleteCategory(999L);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, result.errorCode());
        verify(categoryRepository, never()).delete(any());
        verify(categoryEventProducer, never()).publishCategoryEvent(anyString(), any());
    }
}
