# Borrower — Implementation Context
> Written: 2026-03-30 | Author: Antigravity

## Business Context
This module manages individuals who are authorized to borrow books from the library. It stores essential contact information like email and phone number.

## Technical Decisions
- **Simple POJO**: Borrower is a straightforward entity without authentication or user account logic for now.
- **Unique Email**: Enforced uniqueness in the database at the `email` column level.

## Considered and Rejected
- **Borrower Controller**: Rejected for MVP. Reason: Focus is on borrowing tickets and books. Borrowers are currently fake-seeded.

## Dependencies
- Depends on: None
- Depended by: `BorrowTicket`

## Known Limitations
- ⚠️ Lack of management APIs for borrowers.

## Refactor Log

### 2026-03-30 | Antigravity
- Initial implementation of Borrower model and repository.
- Added data seeding logic.
