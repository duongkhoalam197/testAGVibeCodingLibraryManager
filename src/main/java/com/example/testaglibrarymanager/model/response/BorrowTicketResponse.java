package com.example.testaglibrarymanager.model.response;



import java.time.LocalDateTime;

public record BorrowTicketResponse(
        Long id,
        Long bookId,
        String bookTitle,
        Long borrowerId,
        String borrowerName,
        LocalDateTime borrowDate,
        LocalDateTime returnDate,
        String status
) {}

