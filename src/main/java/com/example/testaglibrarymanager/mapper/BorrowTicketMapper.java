package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;
import com.example.testaglibrarymanager.model.response.BorrowTicketResponse;
import com.example.testaglibrarymanager.model.entity.BorrowTicket;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class BorrowTicketMapper {

    public BorrowTicketDto toDto(BorrowTicket ticket) {
        if (ticket == null)
            return null;

        return new BorrowTicketDto(
                null,
                ticket.getId(),
                ticket.getBook() != null ? ticket.getBook().getId() : null,
                ticket.getBook() != null ? ticket.getBook().getTitle() : null,
                ticket.getBorrower() != null ? ticket.getBorrower().getId() : null,
                ticket.getBorrower() != null ? ticket.getBorrower().getFullName() : null,
                ticket.getBorrowDate(),
                ticket.getReturnDate(),
                ticket.getStatus(),
                null);
    }

    public BorrowTicketDto toEventDto(BorrowTicket ticket, String eventType) {
        if (ticket == null)
            return null;

        return new BorrowTicketDto(
                eventType,
                ticket.getId(),
                ticket.getBook() != null ? ticket.getBook().getId() : null,
                ticket.getBook() != null ? ticket.getBook().getTitle() : null,
                ticket.getBorrower() != null ? ticket.getBorrower().getId() : null,
                ticket.getBorrower() != null ? ticket.getBorrower().getFullName() : null,
                ticket.getBorrowDate(),
                ticket.getReturnDate(),
                ticket.getStatus(),
                LocalDateTime.now());
    }

    public BorrowTicketResponse toResponse(BorrowTicketDto dto) {
        if (dto == null)
            return null;

        return new BorrowTicketResponse(
                dto.id(),
                dto.bookId(),
                dto.bookTitle(),
                dto.borrowerId(),
                dto.borrowerName(),
                dto.borrowDate(),
                dto.returnDate(),
                dto.status().name());
    }
}
