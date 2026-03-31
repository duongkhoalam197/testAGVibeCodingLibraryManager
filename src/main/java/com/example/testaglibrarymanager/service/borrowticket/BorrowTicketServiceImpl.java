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


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BorrowTicketServiceImpl implements BorrowTicketService {

    private final BorrowTicketRepository ticketRepository;
    private final BookRepository bookRepository;
    private final BorrowerRepository borrowerRepository;
    private final BorrowTicketMapper ticketMapper;

    public BorrowTicketServiceImpl(BorrowTicketRepository ticketRepository,
                                   BookRepository bookRepository,
                                   BorrowerRepository borrowerRepository,
                                   BorrowTicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.bookRepository = bookRepository;
        this.borrowerRepository = borrowerRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional
    public ServiceResult<BorrowTicketDto> borrowBook(BorrowRequest request) {
        Optional<Book> bookOpt = bookRepository.findById(request.bookId());
        if (bookOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.BOOK_NOT_FOUND, "Không tìm thấy sách với ID: " + request.bookId());
        }

        Optional<Borrower> borrowerOpt = borrowerRepository.findById(request.borrowerId());
        if (borrowerOpt.isEmpty()) {
            return ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với ID: " + request.borrowerId());
        }

        boolean isAlreadyBorrowed = ticketRepository.existsByBookIdAndStatus(request.bookId(), BorrowTicketStatus.BORROWED);
        if (isAlreadyBorrowed) {
            return ServiceResult.fail(ErrorCode.BOOK_ALREADY_BORROWED, "Cuốn sách này hiện đang được mượn và chưa được trả.");
        }

        BorrowTicket ticket = new BorrowTicket();
        ticket.setBook(bookOpt.get());
        ticket.setBorrower(borrowerOpt.get());
        ticket.setBorrowDate(LocalDateTime.now());
        ticket.setStatus(BorrowTicketStatus.BORROWED);

        return ServiceResult.success(ticketMapper.toDto(ticketRepository.save(ticket)));
    }
}

