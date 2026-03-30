package com.example.testaglibrarymanager.feature.borrowticket.dto;

import com.example.testaglibrarymanager.feature.borrowticket.BorrowTicket;
import com.example.testaglibrarymanager.feature.borrowticket.BorrowTicketStatus;

import java.time.LocalDateTime;

public record BorrowTicketResponse(
        Long id,
        Long bookId,
        Long borrowerId,
        LocalDateTime borrowDate,
        LocalDateTime returnDate,
        BorrowTicketStatus status
) {
    public static BorrowTicketResponse fromEntity(BorrowTicket ticket) {
        return new BorrowTicketResponse(
                ticket.getId(),
                ticket.getBook().getId(),
                ticket.getBorrower().getId(),
                ticket.getBorrowDate(),
                ticket.getReturnDate(),
                ticket.getStatus()
        );
    }
}
