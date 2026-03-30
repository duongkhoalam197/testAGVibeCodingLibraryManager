package com.example.testaglibrarymanager.feature.borrowticket;

import com.example.testaglibrarymanager.exception.InvalidRequestException;
import com.example.testaglibrarymanager.exception.ResourceNotFoundException;
import com.example.testaglibrarymanager.feature.book.Book;
import com.example.testaglibrarymanager.feature.book.BookRepository;
import com.example.testaglibrarymanager.feature.borrower.Borrower;
import com.example.testaglibrarymanager.feature.borrower.BorrowerRepository;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowRequest;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowTicketResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BorrowTicketServiceImpl implements BorrowTicketService {

    private final BorrowTicketRepository ticketRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;

    public BorrowTicketServiceImpl(BorrowTicketRepository ticketRepository,
                                   BookRepository bookRepository,
                                   BorrowerRepository borrowerRepository) {
        this.ticketRepository = ticketRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
    }

    @Override
    @Transactional
    public BorrowTicketResponse borrowBook(BorrowRequest request) {
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", request.bookId()));

        Borrower borrower = borrowerRepository.findById(request.borrowerId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrower", "id", request.borrowerId()));

        boolean isAlreadyBorrowed = ticketRepository.existsByBookIdAndStatus(book.getId(), BorrowTicketStatus.BORROWED);
        if (isAlreadyBorrowed) {
            throw new InvalidRequestException("Cuốn sách này hiện đang được mượn và chưa được trả.");
        }

        BorrowTicket ticket = new BorrowTicket();
        ticket.setBook(book);
        ticket.setBorrower(borrower);
        ticket.setBorrowDate(LocalDateTime.now());
        ticket.setStatus(BorrowTicketStatus.BORROWED);

        return BorrowTicketResponse.fromEntity(ticketRepository.save(ticket));
    }
}
