package com.example.testaglibrarymanager.feature.borrowticket;

import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowRequest;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowTicketResponse;

public interface BorrowTicketService {
    BorrowTicketResponse borrowBook(BorrowRequest request);
}
