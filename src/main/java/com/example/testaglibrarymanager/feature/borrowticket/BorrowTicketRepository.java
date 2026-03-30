package com.example.testaglibrarymanager.feature.borrowticket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicket, Long> {
    boolean existsByBookIdAndStatus(Long bookId, BorrowTicketStatus status);
}
