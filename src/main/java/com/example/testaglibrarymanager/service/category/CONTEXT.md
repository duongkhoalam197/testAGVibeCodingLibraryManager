# Category — Implementation Context
> Updated: 2026-03-31 | Author: Antigravity

## ⚓ Hệ Thống Mỏ Neo Vật Lý
- **Lõi Thực Thể (Entity):** `src/main/java/com/example/testaglibrarymanager/model/entity/Category.java`
- **Bộ Xử Lý (Service):** `src/main/java/com/example/testaglibrarymanager/service/category/CategoryServiceImpl.java`
- **Bộ Phát Sóng (Controller):** `src/main/java/com/example/testaglibrarymanager/controller/CategoryController.java`


## Business Context
Categories provide classification for books. This is a foundational module used to categorize and filter books in the library system.

## Technical Decisions
- **Fake Data Seeding**: Initial categories are seeded directly via `DataInitializer`.
- **Entity Model**: Category ID and name are mapped simply to `categories` table.
- **Result Pattern Validation**: Business validation (like checking if name already exists) is handled in `CategoryServiceImpl` returning a `ServiceResult.fail()`, rather than throwing an Exception.
- **Delete Constraint Handling**: Decided NOT to do manual JPA sub-queries to check for child books before deleting a Category. Instead, relies on Postgres Foreign Key constraint throwing `DataIntegrityViolationException`, caught by `GlobalExceptionHandler` to return `409 Conflict`.

## Considered and Rejected
- **Manual Dependency Checking on Delete**: Rejected checking if `bookRepository.existsByCategoryId()` before deleting a Category. Reason: It duplicates DB constraints and is slower. Postgres handles it better natively.

## Dependencies
- Depends on: None
- Depended by: `Book`

## Known Limitations
- ⚠️ N/A.

## Refactor Log

### 2026-03-30 | Antigravity
- Initial implementation of Category model and repository.
- Added data seeding logic.

### 2026-03-30 | Antigravity (Session 2)
- ⚠️ **Architecture Alert**: This module is currently using the OLD exception-based pattern. It is scheduled for a refactor to the **Result Pattern** (`ServiceResult`) and **SOLID DTO Mapping** to align with the `Book` and `BorrowTicket` features.

### 2026-03-31 | Antigravity (Session 3)
- Refactored full CRUD API.
- Implemented `CategoryServiceImpl` using **Result Pattern**.
- Implemented `CategoryMapper` to enforce SOLID separation (Entity ↔ Response DTO).
- Migrated code from Feature-based `feature/category` structure to **Layered Architecture** (`model/`, `controller/`, `repository/`, `service/`, `mapper/`).
