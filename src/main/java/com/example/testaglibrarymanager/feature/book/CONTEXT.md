# Book — Implementation Context
> Updated: 2026-03-30 | Author: Antigravity

## Business Context
The Book module provides the core inventory management for the library system. It allows librarians to perform CRUD operations on books and filter them based on categories.

## Technical Decisions (Architecture v2)
- **Result Object Pattern**: Service layer no longer throws business exceptions. It returns `ServiceResult<T>` containing `ErrorCode`. Controller maps these to `HttpStatus` via `switch-case`.
- **SOLID Mapping Separation**:
    - **Entity (`Book`)**: JPA only.
    - **Service DTO (`BookDto`)**: Internal service communication.
    - **Response DTO (`BookResponse`)**: Client-facing API contract.
    - **Mapper (`BookMapper`)**: Dedicated component for responsible for all transformations. Ensures `BookResponse` is decoupled from the Entity.
- **Manual Parameter Names**: Explicitly named annotations for Spring 7 reflection compatibility.
- **DTO record Pattern**: Used record for all DTOs to ensure immutability.

## Considered and Rejected
- **Lombok @Data**: Rejected for JPA entities (Risk of circular dependencies/Hash collisions).
- **Exception-Driven Business Logic**: Rejected in favor of the **Result Pattern** (Enterprise/Banking standard) for better performance and explicit control flow.

## Dependencies
- Depends on: `Category`
- Depended by: `BorrowTicket`

## Known Limitations
- ⚠️ N+1 Query in `getAllBooks`. Optimization deferred.

## Refactor Log

### 2026-03-30 | Antigravity
- Initial implementation of Book CRUD.
- **Major Refactor**: Migrated to Result Object Pattern & SOLID DTO Mapping (Separating Response from Entity).
- Added explicit `ErrorCode` handling.
