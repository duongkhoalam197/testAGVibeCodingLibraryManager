# Borrow Ticket Feature Context

## Logic Overview
The `BorrowTicket` feature handles the borrowing process of a book by a borrower.

### Rules mapping
- A book can only be borrowed if it isn't currently borrowed by someone else.
- We perform a realtime check utilizing `BorrowTicketRepository.existsByBookIdAndStatus()`.
- If the book is currently `BORROWED`, the system immediately rejects the request with an `InvalidRequestException` (400 Bad Request).
- Once the validation passes, a new record is created with the `borrowDate` defaulted to `LocalDateTime.now()` and the `status` set to `BORROWED`.

## Dependencies
This feature depends on both `Book` and `Borrower` components to validate if the provided IDs correspond to existing records in the database.
