package com.example.testaglibrarymanager.feature.borrower;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.exception.ErrorCode;
import com.example.testaglibrarymanager.feature.borrower.dto.BorrowerDto;
import com.example.testaglibrarymanager.feature.borrower.dto.CreateBorrowerRequest;
import com.example.testaglibrarymanager.feature.borrower.dto.UpdateBorrowerRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceImplTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private BorrowerMapper borrowerMapper;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @Test
    @DisplayName("Tạo mới thành công người mượn")
    void createBorrower_success_returnsSuccessResult() {
        CreateBorrowerRequest request = new CreateBorrowerRequest("Hoang", "hoang@test.com", "090");
        Borrower entity = new Borrower("Hoang", "hoang@test.com", "090");
        entity.setId(1L);
        BorrowerDto dto = new BorrowerDto(1L, "Hoang", "hoang@test.com", "090");

        when(borrowerRepository.existsByEmail("hoang@test.com")).thenReturn(false);
        when(borrowerMapper.toEntity(request)).thenReturn(new Borrower("Hoang", "hoang@test.com", "090"));
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(entity);
        when(borrowerMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<BorrowerDto> result = borrowerService.createBorrower(request);

        assertTrue(result.isSuccess());
        assertEquals("hoang@test.com", result.data().email());
        verify(borrowerRepository, times(1)).save(any(Borrower.class));
    }

    @Test
    @DisplayName("Tạo mới thất bại do trùng email")
    void createBorrower_duplicateEmail_returnsFail() {
        CreateBorrowerRequest request = new CreateBorrowerRequest("Hoang", "hoang@test.com", "090");
        when(borrowerRepository.existsByEmail("hoang@test.com")).thenReturn(true);

        ServiceResult<BorrowerDto> result = borrowerService.createBorrower(request);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS, result.errorCode());
        verify(borrowerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Lấy thành công theo ID")
    void getBorrowerById_success_returnsSuccessResult() {
        Borrower entity = new Borrower("Hoang", "hoang@test.com", "090");
        entity.setId(1L);
        BorrowerDto dto = new BorrowerDto(1L, "Hoang", "hoang@test.com", "090");

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(borrowerMapper.toDto(entity)).thenReturn(dto);

        ServiceResult<BorrowerDto> result = borrowerService.getBorrowerById(1L);

        assertTrue(result.isSuccess());
        assertEquals("Hoang", result.data().fullName());
    }

    @Test
    @DisplayName("Lấy thất bại do không tìm thấy")
    void getBorrowerById_notFound_returnsFail() {
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        ServiceResult<BorrowerDto> result = borrowerService.getBorrowerById(999L);

        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.BORROWER_NOT_FOUND, result.errorCode());
    }

    @Test
    @DisplayName("Cập nhật thành công")
    void updateBorrower_success_returnsSuccessResult() {
        UpdateBorrowerRequest request = new UpdateBorrowerRequest("Hoang 2", "hoang2@test.com", "091");
        Borrower existingBorrower = new Borrower("Hoang", "hoang@test.com", "090");
        existingBorrower.setId(1L);
        Borrower savedBorrower = new Borrower("Hoang 2", "hoang2@test.com", "091");
        savedBorrower.setId(1L);
        BorrowerDto dto = new BorrowerDto(1L, "Hoang 2", "hoang2@test.com", "091");

        when(borrowerRepository.findById(1L)).thenReturn(Optional.of(existingBorrower));
        when(borrowerRepository.existsByEmail("hoang2@test.com")).thenReturn(false);
        when(borrowerRepository.save(any(Borrower.class))).thenReturn(savedBorrower);
        when(borrowerMapper.toDto(savedBorrower)).thenReturn(dto);

        ServiceResult<BorrowerDto> result = borrowerService.updateBorrower(1L, request);

        assertTrue(result.isSuccess());
        assertEquals("hoang2@test.com", result.data().email());
    }

    @Test
    @DisplayName("Xóa thành công")
    void deleteBorrower_success_returnsSuccessResult() {
        when(borrowerRepository.existsById(1L)).thenReturn(true);

        ServiceResult<Void> result = borrowerService.deleteBorrower(1L);

        assertTrue(result.isSuccess());
        verify(borrowerRepository, times(1)).deleteById(1L);
    }
}
