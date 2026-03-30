# Architecture

> System design overview. Update only during architecture review sessions.

---

## High-Level Architecture

```
                         ┌─────────────────┐
                         │   Client (SPA)   │
                         └────────┬─────────┘
                                  │ HTTPS
                                  ▼
                         ┌─────────────────┐
                         │   Spring Boot    │
                         │   Application    │
                         └────────┬─────────┘
                                  │
              ┌───────────────────┼───────────────────┐
              ▼                   ▼                    ▼
     ┌────────────────┐  ┌────────────────┐  ┌────────────────┐
     │   Controller   │  │ Validation Flow│  │ Exception      │
     │   (REST API)   │  │ (@Valid)       │  │ Handler        │
     └───────┬────────┘  └────────────────┘  └────────────────┘
             │
             ▼
     ┌────────────────┐
     │   Service      │
     │ (Interface +   │
     │  Impl)         │
     └───────┬────────┘
             │
             ▼
     ┌────────────────┐
     │  Repository    │
     │  (Spring Data) │
     └───────┬────────┘
             │
             ▼
     ┌────────────────┐
     │  PostgreSQL    │
     └────────────────┘
```

---

## Request Flow

### Standard CRUD Request
```
Client
  → [HTTP Request GET/POST/PUT/DELETE]
  → Controller (receive request, validate constraints via @Valid)
  → Service (execute business logic, entity ↔ DTO conversion)
  → Repository (JPA queries)
  → Database
  → Repository (returns Entity objects)
  → Service (converts Entity → Response DTOs)
  → Controller (wraps result with ApiResponse<T>)
  → [HTTP Response 200/201]
  → Client
```

---

## Feature Package Structure

Each business feature is self-contained:

```
feature/
├── category/                # Category CRUD & fake data
├── book/                    # Book CRUD
├── borrower/                # Borrower CRUD & fake data
└── borrowticket/            # Ticket logic (borrowing and returning)
```

### Feature Dependencies

```
category  ← (no dependencies)
     ↑
   book     ← category (ManyToOne: book depends on category)

borrower  ← (no dependencies)

borrowticket ← book (check availability)
     │
     └────── ← borrower (assigned to a person)
```

Dependency rules:
- `category` and `borrower` are independent — no dependencies on other features.
- `book` relies on `category` for classification.
- `borrowticket` relies on `book` and `borrower`.
- **No circular dependencies allowed.**

---

## Cross-Cutting Concerns

### Exception Handling
- `GlobalExceptionHandler` (`@RestControllerAdvice`) catches all exceptions.
- All responses use `ApiResponse<T>` wrapper — including errors.
- No stack traces or SQL errors exposed to client.

### DTO Strategy
- Request/Response DTOs are Java Records.
- Entity never exposed outside Service layer.
- Response DTO has `static fromEntity()` factory method.
- Feature-specific DTOs live inside feature's `dto/` sub-package.