package com.example.testaglibrarymanager.feature.borrowticket;

import com.example.testaglibrarymanager.exception.InvalidRequestException;
import com.example.testaglibrarymanager.exception.ResourceNotFoundException;
import com.example.testaglibrarymanager.feature.book.Book;
import com.example.testaglibrarymanager.feature.book.BookRepository;
import com.example.testaglibrarymanager.feature.borrower.Borrower;
import com.example.testaglibrarymanager.feature.borrower.BorrowerRepository;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowRequest;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowTicketResponse;
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

    @InjectMocks
    private BorrowTicketServiceImpl ticketService;

    @Test
    @DisplayName("Gặp lỗi ném ra ResourceNotFoundException khi sách (Book) không tồn tại")
    void borrowBook_bookNotFound_throwsException() {
        // Arrange
        BorrowRequest request = new BorrowRequest(999L, 1L);
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> ticketService.borrowBook(request));
        assertTrue(exception.getMessage().contains("Book not found"));
        verify(bookRepository, times(1)).findById(999L);
        verifyNoInteractions(borrowerRepository, ticketRepository);
    }

    @Test
    @DisplayName("Gặp lỗi ném ra ResourceNotFoundException khi người mượn (Borrower) không tồn tại")
    void borrowBook_borrowerNotFound_throwsException() {
        // Arrange
        BorrowRequest request = new BorrowRequest(1L, 999L);
        Book mockBook = new Book();
        mockBook.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> ticketService.borrowBook(request));
        assertTrue(exception.getMessage().contains("Borrower not found"));
        verify(bookRepository, times(1)).findById(1L);
        verify(borrowerRepository, times(1)).findById(999L);
        verifyNoInteractions(ticketRepository);
    }

    @Test
    @DisplayName("Gặp lỗi ném ra InvalidRequestException khi sách hiện CÒN ĐANG được mượn")
    void borrowBook_bookAlreadyBorrowed_throwsException() {
        // Arrange
        BorrowRequest request = new BorrowRequest(1L, 2L);
        
        Book mockBook = new Book();
        mockBook.setId(1L);
        
        Borrower mockBorrower = new Borrower();
        mockBorrower.setId(2L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(2L)).thenReturn(Optional.of(mockBorrower));
        // Giả lập sách đang bị mượn
        when(ticketRepository.existsByBookIdAndStatus(1L, BorrowTicketStatus.BORROWED)).thenReturn(true);

        // Act & Assert
        Exception exception = assertThrows(InvalidRequestException.class, () -> ticketService.borrowBook(request));
        assertTrue(exception.getMessage().contains("Cuốn sách này hiện đang được mượn"));
        verify(ticketRepository, times(1)).existsByBookIdAndStatus(1L, BorrowTicketStatus.BORROWED);
        verify(ticketRepository, never()).save(any());
    }

    @Test
    @DisplayName("Thành công mượn sách, trả về Request DTO hợp lệ")
    void borrowBook_success_returnsTicketResponse() {
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

        when(bookRepository.findById(10L)).thenReturn(Optional.of(mockBook));
        when(borrowerRepository.findById(20L)).thenReturn(Optional.of(mockBorrower));
        when(ticketRepository.existsByBookIdAndStatus(10L, BorrowTicketStatus.BORROWED)).thenReturn(false);
        when(ticketRepository.save(any(BorrowTicket.class))).thenReturn(savedTicket);

        // Act
        BorrowTicketResponse response = ticketService.borrowBook(request);

        // Assert
        assertNotNull(response);
        assertEquals(100L, response.id());
        assertEquals(10L, response.bookId());
        assertEquals(20L, response.borrowerId());
        assertEquals(BorrowTicketStatus.BORROWED, response.status());
        assertNotNull(response.borrowDate());
        
        verify(ticketRepository, times(1)).save(any(BorrowTicket.class));
    }
}
