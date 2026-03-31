package com.example.testaglibrarymanager.repository;

import com.example.testaglibrarymanager.model.entity.BorrowTicketStatus;
import com.example.testaglibrarymanager.model.entity.BorrowTicket;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicket, Long> {
    boolean existsByBookIdAndStatus(Long bookId, BorrowTicketStatus status);
}

