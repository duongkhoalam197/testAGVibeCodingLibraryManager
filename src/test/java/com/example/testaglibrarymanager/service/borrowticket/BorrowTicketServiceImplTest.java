package com.example.testaglibrarymanager.service.borrowticket;

import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.repository.BorrowTicketRepository;
import com.example.testaglibrarymanager.mapper.BorrowTicketMapper;
import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;
import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.repository.BorrowerRepository;
import com.example.testaglibrarymanager.repository.BookRepository;
import com.example.testaglibrarymanager.model.entity.Book;
import com.example.testaglibrarymanager.model.entity.BorrowTicketStatus;
import com.example.testaglibrarymanager.util.exception.ErrorCode;
import com.example.testaglibrarymanager.model.request.BorrowRequest;
import com.example.testaglibrarymanager.model.entity.BorrowTicket;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowTicketServiceImplTest {

    @Mock
    private BorrowTicketRepository ticketRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BorrowerRepository borrowerRepository;
    @Mock
    private BorrowTicketMapper ticketMapper;

    @InjectMocks
    private BorrowTicketServiceImpl ticketService;

    @Test
    @DisplayName("Trả về thất bại khi sách (Book) không tồn tại")
    void borrowBook_bookNotFound_returnsFail() {
        // Arrange
        BorrowRequest request = new BorrowRequest(999L, 1L);
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ServiceResult<BorrowTicketDto> result = ticketService.borrowBook(request);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.BOOK_NOT_FOUND, result.errorCode());
        verify(bookRepository, times(1)).findById(999L);
        verifyNoInteractions(borrowerRepository, ticketRepository, ticketMapper);
    }

    @Test
    @DisplayName("Trả về thất bại khi người mượn (Borrower) không tồn tại")
    void borrowBook_borrowerNotFound_returnsFail() {
        // Arrange
        BorrowRequest request = new BorrowRequest(1L, 999L);
        Book mockBook = new Book();
        mockBook.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        ServiceResult<BorrowTicketDto> result = ticketService.borrowBook(request);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.BORROWER_NOT_FOUND, result.errorCode());
        verify(bookRepository, times(1)).findById(1L);
        verify(borrowerRepository, times(1)).findById(999L);
        verifyNoInteractions(ticketRepository, ticketMapper);
    }

    @Test
    @DisplayName("Trả về thất bại khi sách hiện CÒN ĐANG được mượn")
    void borrowBook_bookAlreadyBorrowed_returnsFail() {
        // Arrange
        BorrowRequest request = new BorrowRequest(1L, 2L);
        
        Book mockBook = new Book();
        mockBook.setId(1L);
        
        Borrower mockBorrower = new Borrower();
        mockBorrower.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(2L)).thenReturn(Optional.of(mockBorrower));
        when(ticketRepository.existsByBookIdAndStatus(1L, BorrowTicketStatus.BORROWED)).thenReturn(true);

        // Act
        ServiceResult<BorrowTicketDto> result = ticketService.borrowBook(request);

        // Assert
        assertFalse(result.isSuccess());
        assertEquals(ErrorCode.BOOK_ALREADY_BORROWED, result.errorCode());
        verify(ticketRepository, times(1)).existsByBookIdAndStatus(1L, BorrowTicketStatus.BORROWED);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("Thành công mượn sách, trả về Result thành công kèm DTO")
    void borrowBook_success_returnsSuccessResult() {
        // Arrange
        BorrowRequest request = new BorrowRequest(10L, 20L);
        
        Book mockBook = new Book();
        mockBook.setId(10L);
        
        Borrower mockBorrower = new Borrower();
        mockBorrower.setId(20L);

        BorrowTicket savedTicket = new BorrowTicket();
        savedTicket.setId(100L);
        savedTicket.setBook(mockBook);
        savedTicket.setBorrower(mockBorrower);
        savedTicket.setBorrowDate(LocalDateTime.now());
        savedTicket.setStatus(BorrowTicketStatus.BORROWED);

        BorrowTicketDto mockDto = new BorrowTicketDto(100L, 10L, "Book Title", 20L, "Borrower Name", LocalDateTime.now(), null, BorrowTicketStatus.BORROWED);

        when(bookRepository.findById(10L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(20L)).thenReturn(Optional.of(mockBorrower));
        when(ticketRepository.existsByBookIdAndStatus(10L, BorrowTicketStatus.BORROWED)).thenReturn(false);
        when(ticketRepository.save(any(BorrowTicket.class))).thenReturn(savedTicket);
        when(ticketMapper.toDto(any(BorrowTicket.class))).thenReturn(mockDto);

        // Act
        ServiceResult<BorrowTicketDto> result = ticketService.borrowBook(request);

        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.data());
        assertEquals(100L, result.data().id());
        assertEquals(BorrowTicketStatus.BORROWED, result.data().status());
        
        verify(ticketRepository, times(1)).save(any(BorrowTicket.class));
        verify(ticketMapper, times(1)).toDto(any(BorrowTicket.class));
    }
}

