# Book — Implementation Context
> Written: 2026-03-30 | Author: Antigravity

## Business Context
The Book module provides the core inventory management for the library system. It allows librarians to perform CRUD operations on books and filter them based on categories.

## Technical Decisions
- **Manual Parameter Names**: @RequestParam("categoryId") and @PathVariable("id") explicitly named. Reason: Spring Boot 4/Spring 7 no longer auto-detects parameter names from bytecode without `-parameters` flag.
- **DTO record Pattern**: Used record for `BookRequest` and `BookResponse`. Reason: Ensures immutability and reduces boilerplate.
- **Lazy Fetching**: Book's category is marked as LAZY. Reason: Prevents fetching unnecessary data for single book operations.

## Considered and Rejected
- **Lombok @Data**: Rejected because it generates `equals/hashCode` on all fields, which can cause issues with JPA entity state management and infinite recursion in bidirectional relationships.

## Dependencies
- Depends on: `Category`
- Depended by: `BorrowTicket`

## Known Limitations
- ⚠️ N+1 Query in `getAllBooks`. Reason: For MVP, simple findAll is sufficient as the dataset is currently small. Optimization via Join Fetch or Entity Graph is deferred.

## Refactor Log

### 2026-03-30 | Antigravity
- Initial implementation of Book CRUD.
- Fixed 500 error by adding explicit names to annotations.
