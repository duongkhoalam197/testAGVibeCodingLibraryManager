package com.example.testaglibrarymanager.controller;

import com.example.testaglibrarymanager.service.borrowticket.BorrowTicketService;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.mapper.BorrowTicketMapper;
import com.example.testaglibrarymanager.model.dto.BorrowTicketDto;
import com.example.testaglibrarymanager.model.dto.ApiResponse;
import com.example.testaglibrarymanager.model.response.BorrowTicketResponse;
import com.example.testaglibrarymanager.model.request.BorrowRequest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tickets")
public class BorrowTicketController {

    private final BorrowTicketService ticketService;
    private final BorrowTicketMapper ticketMapper;

    public BorrowTicketController(BorrowTicketService ticketService, BorrowTicketMapper ticketMapper) {
        this.ticketService = ticketService;
        this.ticketMapper = ticketMapper;
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<BorrowTicketResponse>> borrowBook(@RequestBody BorrowRequest request) {
        ServiceResult<BorrowTicketDto> result = ticketService.borrowBook(request);
        
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(ticketMapper.toResponse(result.data())));
        }

        HttpStatus status = switch (result.errorCode()) {
            case BOOK_NOT_FOUND, BORROWER_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BOOK_ALREADY_BORROWED -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
        
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), result.errorCode().name(), result.message()));
    }
}

