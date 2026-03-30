package com.example.testaglibrarymanager.feature.borrowticket;

import com.example.testaglibrarymanager.dto.ApiResponse;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowRequest;
import com.example.testaglibrarymanager.feature.borrowticket.dto.BorrowTicketResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class BorrowTicketController {

    private final BorrowTicketService ticketService;

    public BorrowTicketController(BorrowTicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/borrow")
    public ResponseEntity<ApiResponse<BorrowTicketResponse>> borrowBook(@RequestBody @Valid BorrowRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(ticketService.borrowBook(request)));
    }
}
