# Borrower — Implementation Context
> Updated: 2026-03-31 | Author: Antigravity

## ⚓ Hệ Thống Mỏ Neo Vật Lý
- **Lõi Thực Thể (Entity):** `src/main/java/com/example/testaglibrarymanager/model/entity/Borrower.java`
- **Bộ Xử Lý (Service):** `src/main/java/com/example/testaglibrarymanager/service/borrower/BorrowerServiceImpl.java`
- **Bộ Phát Sóng (Controller):** `src/main/java/com/example/testaglibrarymanager/controller/BorrowerController.java`


## Business Context
This module manages individuals who are authorized to borrow books from the library. It stores essential contact information like email and phone number.

## Technical Decisions
- **Simple POJO**: Borrower is a straightforward entity without authentication or user account logic for now.
- **Unique Email**: Enforced uniqueness in the database at the `email` column level AND validated at the API logic level (returning `BORROWER_EMAIL_ALREADY_EXISTS`).
- **Result Pattern Validation**: Business validation (like checking if email already exists) is handled in `BorrowerServiceImpl` returning a `ServiceResult.fail()`.
- **Constraint Handling**: Similar to Category, deleting a Borrower linked to an existing BorrowTicket will cause a PostgreSQL `Foreign Key Violation`. We rely on `GlobalExceptionHandler` to translate this into a `409 Conflict`.

## Considered and Rejected
- **Manual Ticket Checks on Delete**: Rejected querying the database to ensure no tickets exist before deleting the Borrower. Postgres will safely bounce the request if relations exist, saving an unnecessary query roundtrip.

## Dependencies
- Depends on: None
- Depended by: `BorrowTicket`

## Known Limitations
- ⚠️ N/A.

## Refactor Log

### 2026-03-30 | Antigravity
- Initial implementation of Borrower model and repository.
- Added data seeding logic.

### 2026-03-30 | Antigravity (Session 2)
- ⚠️ **Architecture Alert**: Borrower is currently using the OLD Exception-driven pattern. Needs a refactor to the **Result Pattern** and **SOLID DTO Mapping** to match the project's new Enterprise standard.

### 2026-03-31 | Antigravity (Session 3)
- Refactored full CRUD API.
- Implemented `BorrowerServiceImpl` using **Result Pattern**.
- Implemented `BorrowerMapper` to enforce SOLID separation (Entity ↔ Response DTO).
- Migrated code from Feature-based `feature/borrower` structure to **Layered Architecture** (`model/`, `controller/`, `repository/`, `service/`, `mapper/`).
