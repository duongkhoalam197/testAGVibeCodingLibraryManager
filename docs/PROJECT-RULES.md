# Project Rules — Spring Boot 4 + Library Management

> Coding conventions and best practices. Both AI and developer must follow.
> This is the single source of truth — all AI tools (Claude, Cursor, Copilot, Gemini) read this file.

---

## 0. Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 4.x / Spring Framework 7 |
| Database | Spring Data JPA + PostgreSQL |
| Build | Maven |
| Validation | Jakarta Bean Validation |

---

## 1. Package Structure

```
com.example.testaglibrarymanager/
│
├── TestAgLibraryManagerApplication.java
│
├── config/                              # All configuration classes
│   └── DataInitializer.java             # Fake data for Categories/Borrowers
│
├── exception/                           # Global exception handling
│   ├── GlobalExceptionHandler.java      # @RestControllerAdvice
│   ├── AppException.java                # Base custom exception
│   ├── ResourceNotFoundException.java
│   └── InvalidRequestException.java
│
├── dto/                                 # Shared DTOs
│   ├── ApiResponse.java                 # Standard response wrapper
│
├── feature/                             # Business features
│   ├── book/
│   │   ├── BookController.java
│   │   ├── BookService.java             # Interface
│   │   ├── BookServiceImpl.java         # Implementation
│   │   ├── BookRepository.java
│   │   ├── Book.java                    # Entity
│   │   └── dto/
│   │       ├── BookRequest.java
│   │       └── BookResponse.java
│   │
│   ├── borrowticket/
│   │   ├── BorrowTicketController.java
│   │   ├── BorrowTicketService.java     # Interface
│   │   ├── BorrowTicketServiceImpl.java # Implementation
│   │   ├── BorrowTicketRepository.java
│   │   ├── BorrowTicket.java            # Entity
│   │   └── dto/
│   │       ├── BorrowRequest.java
│   │       └── BorrowTicketResponse.java
│   │
│   ├── category/
│   │   ├── CategoryRepository.java
│   │   └── Category.java
│   │
│   └── borrower/
│       ├── BorrowerRepository.java
│       └── Borrower.java
│
├── util/                                # Utility classes (pure static, no state)
│
└── resources/
    ├── application.properties           # PostgreSQL config
```

### Package Rules
- 1 feature = 1 package containing controller, service (interface + impl), repository, entity, DTOs
- Feature DTOs stay inside the feature's `dto/` sub-package, NOT in the shared `dto/`
- Shared `dto/` only contains cross-cutting DTOs like `ApiResponse`

---

## 2. Naming Convention

### Classes
| Type | Pattern | Example |
|---|---|---|
| Entity | `[Name]` | `Book`, `Category` |
| Controller | `[Feature]Controller` | `BookController`, `BorrowTicketController` |
| Service interface | `[Feature]Service` | `BookService` |
| Service impl | `[Feature]ServiceImpl` | `BookServiceImpl` |
| Repository | `[Feature]Repository` | `BookRepository` |
| DTO request | `[Action/Feature]Request` | `BookRequest`, `BorrowRequest` |
| DTO response | `[Feature]Response` | `BookResponse`, `BorrowTicketResponse` |
| Exception | `[Name]Exception` | `ResourceNotFoundException` |

### Methods
| Layer | Convention | Example |
|---|---|---|
| Controller | HTTP verb-oriented | `getBook()`, `createBook()`, `deleteBook()` |
| Service | Business action | `borrowBook()`, `createBook()` |
| Repository | Spring Data convention | `findActiveTicketsByBookId()` |

### Variables
- `camelCase` for all variables and methods
- Meaningful names, avoid abbreviations: `bookRepository` not `bookRepo` or `br`
- Boolean prefix: `is/has/can` — `isActive`

---

## 3. Service Layer — Interface + Implementation

### Rules
- Service interface defines the contract — NO implementation details
- `@Service` annotation on Impl class, NOT on interface
- `@Transactional` on Impl methods that write data
- Controller depends on interface: `private final BookService bookService`
- Service MUST NOT know about HTTP (no HttpServletRequest, ResponseEntity, HttpStatus)
- Entity → DTO conversion happens in service layer

---

## 4. Controller Layer

### Rules
- Controller does exactly 3 things: receive request → call service → return response
- NO business logic in controller
- ALWAYS use `@Valid` on `@RequestBody`
- ALWAYS return `ResponseEntity<ApiResponse<T>>`
- ALWAYS use constructor injection — declare `final` fields, write explicit constructor
- NEVER use `@Autowired` field injection
- Inject interface, NOT implementation class

---

## 5. ApiResponse Wrapper

```java
public record ApiResponse<T>(
        int statusCode,
        T data,
        String message,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), data, "Success", LocalDateTime.now());
    }
    public static <T> ApiResponse<T> error(int statusCode, String message) {
        return new ApiResponse<>(statusCode, null, message, LocalDateTime.now());
    }
}
```

### Rules
- ALL endpoints return `ApiResponse<T>` — no exceptions

---

## 6. DTO Rules — Java Records

### Rules
- Use `record` for all DTOs — immutable by design
- Request DTOs: ALWAYS have Jakarta Bean Validation annotations
- Response DTOs: have `static fromEntity()` conversion method
- NEVER expose Entity directly outside the service layer
- NEVER put `@Entity` or JPA annotations on DTOs

---

## 7. Entity Rules

### Rules
- Explicit `@Table(name = "...")` with snake_case table name
- Explicit `@Column(nullable, length, unique)` constraints
- `@Enumerated(EnumType.STRING)` — NEVER use ORDINAL
- Write explicit no-arg constructor (required by JPA), getters, and setters for each field
- Do NOT use `@Data`/Lombok on Entities — it generates `equals/hashCode` on all fields, causing JPA issues
- `createdAt` / `updatedAt`: expose getter only (if used)

---

## 8. Exception Handling

### Rules
- All custom exceptions extend `AppException`
- `@RestControllerAdvice` handles ALL exceptions — controllers do NOT try/catch
- NEVER expose stack traces, SQL errors, or internal details to client

---

## 9. Repository Layer

### Rules
- Use Spring Data derived query methods when possible
- Return `Optional<T>` for single results — NEVER return null
- No business logic in repository

---

## 10. Logging

### Rules
- Declare logger as `private static final Logger log = LoggerFactory.getLogger(ClassName.class)`
- NEVER log: personal identifiable info
- Use parameterized `{}` — NEVER string concatenation in log statements
- Controller: no logging needed (HTTP access logs cover it)
- Service: log important business events

---

## 15. Code Size Limits

| Metric | Limit | Action if exceeded |
|---|---|---|
| File | < 300 lines | Split into smaller classes |
| Method | < 50 lines | Extract methods |
| Method parameters | < 5 | Group into a record/object |
| Constructor parameters | < 7 | Split responsibilities into separate services |
| Nested blocks (if/for) | < 3 levels | Use early return or extract method |

---

## 16. Commit Checklist

- [ ] No `@Autowired` field injection — constructor injection only (explicit constructor)
- [ ] No Entity returned from controller — DTO records used
- [ ] `@Valid` on every `@RequestBody`
- [ ] Custom exceptions used — no raw `RuntimeException`
- [ ] No hardcoded config — `application.yml` + env variables
- [ ] No sensitive data in JWT claims or logs
- [ ] Service layer uses Interface + Impl pattern
- [ ] All responses wrapped in `ApiResponse`
- [ ] Tests cover happy path + error cases
- [ ] File < 300 lines, method < 50 lines
- [ ] `CONTEXT.md` updated if important logic changed
- [ ] `PROJECT-STATUS.md` updated

---

## 17. Documentation Requirements

| When | Action |
|------|--------|
| End of every coding session | Update `docs/PROJECT-STATUS.md` |
| New feature with non-obvious logic | Create `CONTEXT.md` inside feature package |
| Architecture decision | Create new file in `docs/decisions/` |
| New API endpoints | Update `docs/API_SPEC.md` |
| Schema changes | Update `docs/DATABASE.md` |