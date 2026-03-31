package com.example.testaglibrarymanager.service.borrowticket;

import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;
import com.example.testaglibrarymanager.model.request.BorrowRequest;



public interface BorrowTicketService {
    ServiceResult<BorrowTicketDto> borrowBook(BorrowRequest request);
}

