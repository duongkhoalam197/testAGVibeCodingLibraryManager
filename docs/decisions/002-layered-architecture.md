# System Architecture — Implementation Context
> Written: 2026-03-31 | Author: Antigravity

## Business Context
This document captures the rationale for the overall Spring Boot structure and design patterns used across the entire Library Management System APIs. It serves as a master reference for structural decisions.

## Technical Decisions
- **Layered Packaging (Model, Controller, Service, Repository, Mapper)**: Initially started as Feature-based layout (`feature/*`). Refactored to Layered Architecture on Day 2. Reason: To ensure strict separation of concerns, preventing business logic from leaking into models and ensuring a standardized location for DTOs and Mappers.
- **Result Pattern (`ServiceResult<T>`)**: We do not throw exceptions for predicted business violations (e.g., "Email exists", "Book not available"). Controllers read `ServiceResult.isSuccess()` to return appropriate JSON structures instead. Reason: Avoids using Java Exceptions for control flow (which is slow and hard to trace).
- **SOLID Entity ↔ DTO Isolation**: Entities (`Book`, `Borrower`) NEVER touch the `Controller` layer. All conversions are explicitly handled via dedicated `Mapper` classes (`BookMapper`, etc.). Reason: Changing the DB schema should never accidentally leak or break the JSON API contract.
- **GlobalExceptionHandler**: Used exlusively to catch infrastructure/system errors like `MethodArgumentNotValidException` (Validation) and `DataIntegrityViolationException` (PostgreSQL 409 Foreign Key violation).

## Considered and Rejected
- **Feature-Based Packaging (`feature/*`)**: Initially evaluated and built. Rejected later because placing `Entity`, `Service`, `Controller`, and `DTO` side-by-side in one directory creates "God Packages" when features start getting large, making it harder to establish clear module boundaries (especially when many features share identical DTO mapping structures).
- **Service Logic inside DTOs (`fromEntity()` inside Record)**: Evaluated for simplicity. Rejected because DTOs should be dumb data carriers, and converting Entities often requires injecting other services/repositories, which records cannot cleanly do. Moving them to `Mapper` classes fixes this.

## Dependencies
- Affects: Entire codebase.

## Known Limitations
- ⚠️ Grouping `service` into a single flat layer may cause the package to balloon as more features are added. When this happens, sub-packages like `service.book` or `service.auth` should be established INSTEAD of returning to a raw Feature-based approach.

## Refactor Log

### 2026-03-31 | Antigravity
- Executed the "Great Refactoring" via Python/Java script migration.
- Moved all features from `feature/*` into their respective layers.
- Centralized all `CONTEXT.md` files to `docs/context/`.
