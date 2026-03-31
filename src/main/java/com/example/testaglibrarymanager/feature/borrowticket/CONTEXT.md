# Borrow Ticket Feature Context
> Updated: 2026-03-30 | Author: Antigravity

## Logic Overview (Architecture v2)
The `BorrowTicket` feature handles the borrowing process of a book by a borrower.

### Business Rules mapping
- A book can only be borrowed if it isn't currently borrowed (`BORROWED` status).
- We perform a realtime check utilizing `BorrowTicketRepository.existsByBookIdAndStatus()`.
- **Result Pattern**: Instead of `InvalidRequestException`, the system returns a `ServiceResult.fail(ErrorCode.BOOK_ALREADY_BORROWED)`.
- The **BorrowTicketController** is responsible for mapping this `ErrorCode` into a `409 Conflict` (for already borrowed) or `404 Not Found` (for missing book/borrower).

## Technical Decisions
- **SOLID Mapping Isolation**:
    - **Service Layer**: Returns `BorrowTicketDto` (Service DTO).
    - **Controller Layer**: Maps `BorrowTicketDto` -> `BorrowTicketResponse` via `BorrowTicketMapper`.
    - Result: Client-side response has no direct link to the Entity layer.
- **Transactional Consistency**: `@Transactional` used on Service to ensure atomic ticket creation and status check.

## Dependencies
This feature depends on both `Book` and `Borrower` components. It handles checking their existence and returning `ErrorCode.BOOK_NOT_FOUND` or `ErrorCode.BORROWER_NOT_FOUND` appropriately.

## Refactor Log / 2026-03-30
- Initial implementation with exception-based logic.
- **Major Refactor**: Migrated to `ServiceResult` Pattern and separated ServiceDTO from ResponseDTO.
- Updated `BorrowTicketServiceImplTest` to cover the new result-based assertions.
