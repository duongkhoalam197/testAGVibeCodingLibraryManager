package com.example.testaglibrarymanager.model.request;

import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.model.entity.Book;


import jakarta.validation.constraints.NotNull;

public record BorrowRequest(
        @NotNull(message = "Book ID is required")
        Long bookId,

        @NotNull(message = "Borrower ID is required")
        Long borrowerId
) {
}

