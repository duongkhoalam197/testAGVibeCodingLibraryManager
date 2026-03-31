# API Specification

> All endpoints return `ApiResponse<T>` wrapper.
> Update this file whenever endpoints change.

---

## Base URL

```
Development: http://localhost:8080/api
```

---

## Standard Response Structure (Architecture v2)
Every response follows this JSON pattern:
```json
{
  "statusCode": 200,          // HTTP Status Code
  "code": "SUCCESS",         // Domain Error Code (e.g. BOOK_NOT_FOUND, VALIDATION_FAILED, SUCCESS)
  "data": { ... },           // Payload T
  "message": "Thành công",    // Human readable message
  "timestamp": "20xx-02-28T10:00:00"
}
```

---

## 1. Categories
### GET /api/categories
List all categories.

### GET /api/categories/{id}
Get a single category by ID.

### POST /api/categories
Create a new category.
**Request Body:**
```json
{
  "name": "Manga"
}
```
**Errors:**
- `409 CATEGORY_ALREADY_EXISTS`

### PUT /api/categories/{id}
Update an existing category.

### DELETE /api/categories/{id}
Hard delete a category.
**Errors:**
- `409 DATABASE_CONSTRAINT_VIOLATION` (If the category is linked to an existing Book).

---

## 2. Borrowers
### GET /api/borrowers
List all borrowers.

### GET /api/borrowers/{id}
Get a single borrower by ID.

### POST /api/borrowers
Create a new borrower.
**Request Body:**
```json
{
  "fullName": "Le Van C",
  "email": "c@example.com",
  "phone": "0987654321"
}
```
**Errors:**
- `409 BORROWER_EMAIL_ALREADY_EXISTS`

### PUT /api/borrowers/{id}
Update an existing borrower.

### DELETE /api/borrowers/{id}
Hard delete a borrower.
**Errors:**
- `409 DATABASE_CONSTRAINT_VIOLATION` (If the borrower has existing BorrowTickets).

---

## 3. Books

### GET /api/books
List all books. Can filter by Category ID via query param.

**Query Parameters:**
| Param | Type | Default | Description |
|-------|------|---------|-------------|
| categoryId | Long | null | Filter by Category |

### GET /api/books/{id}
Get a single book by ID.

### POST /api/books
Create a new book.
**Request Body:**
```json
{
  "title": "The Pragmatic Programmer",
  "author": "Andy Hunt",
  "price": 45.0,
  "categoryId": 1
}
```

---

## 4. BorrowTicket

### POST /api/tickets/borrow
Creates a record allowing a user to borrow a specific book. Ensures real-time validation.

**Request Body:**
```json
{
  "bookId": 1,
  "borrowerId": 1
}
```

**Validation Rules:**
- `bookId` and `borrowerId` must exist.
- The Book must NOT be currently borrowed (No existing Ticket for this Book with `status == BORROWED`).

**Errors:**
| Status | Code | When |
|--------|------|------|
| 409 | BOOK_ALREADY_BORROWED | Book already borrowed |
| 404 | BOOK_NOT_FOUND | Book not found |
| 404 | BORROWER_NOT_FOUND | Borrower not found |
