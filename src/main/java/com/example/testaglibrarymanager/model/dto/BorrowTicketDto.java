package com.example.testaglibrarymanager.model.dto;

import com.example.testaglibrarymanager.model.entity.BorrowTicketStatus;


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

