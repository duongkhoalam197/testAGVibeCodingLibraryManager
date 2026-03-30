# API Specification

> All endpoints return `ApiResponse<T>` wrapper.
> Update this file whenever endpoints change.

---

## Base URL

```
Development: http://localhost:8080/api
```

---

## 1. Books

### GET /api/books

List all books. Can filter by Category ID via query param.

**Query Parameters:**
| Param | Type | Default | Description |
|-------|------|---------|-------------|
| categoryId | Long | null | Filter by Category |

**Success Response (200):**
```json
{
  "statusCode": 200,
  "data": [
      {
        "id": 1,
        "title": "Clean Code",
        "author": "Robert C. Martin",
        "price": 30.5,
        "category": { "id": 1, "name": "IT / Tech" }
      }
  ],
  "message": "Books fetched successfully",
  "timestamp": "20xx-02-28T10:00:00"
}
```

---

### GET /api/books/{id}

Get a single book by ID.

**Success Response (200):**
```json
{ ... } // Single book object in data
```

**Errors:**
| Status | When |
|--------|------|
| 404 | Book not found |

---

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

**Success Response (201):**

---

### PUT /api/books/{id}

Update an existing book.

**Request Body:**
```json
{
  "title": "Clean Architecture",
  "author": "Robert C. Martin",
  "price": 50.0,
  "categoryId": 1
}
```

---

### DELETE /api/books/{id}

Hard Delete a book.

**Success Response (200):**
```json
{
  "statusCode": 200,
  "data": null,
  "message": "Book deleted successfully",
  "timestamp": "20xx-02-28T12:00:00"
}
```

---

## 2. BorrowTicket

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

**Success Response (201):**
```json
{
  "statusCode": 201,
  "data": {
    "id": 1,
    "bookId": 1,
    "borrowerId": 1,
    "borrowDate": "20xx-02-28T10:00:00Z",
    "returnDate": null,
    "status": "BORROWED"
  },
  "message": "Book borrowed successfully",
  "timestamp": "20xx-02-28T10:00:00"
}
```

**Errors:**
| Status | When |
|--------|------|
| 400 | Validation failed (Book already borrowed, invalid inputs) |
| 404 | Book or Borrower not found |
