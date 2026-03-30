package com.example.testaglibrarymanager.feature.borrowticket.dto;

import com.example.testaglibrarymanager.feature.borrowticket.BorrowTicketStatus;
import java.time.LocalDateTime;

public record BorrowTicketDto(
        Long id,
        Long bookId,
        String bookTitle,
        Long borrowerId,
        String borrowerName,
        LocalDateTime borrowDate,
        LocalDateTime returnDate,
        BorrowTicketStatus status
) {}
