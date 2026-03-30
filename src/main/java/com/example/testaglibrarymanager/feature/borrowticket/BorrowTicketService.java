package com.example.testaglibrarymanager.feature.borrowticket;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowRequest;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowTicketDto;

public interface BorrowTicketService {
    ServiceResult<BorrowTicketDto> borrowBook(BorrowRequest request);
}
