# Database Schema

> PostgreSQL database design for Library Management System
> Update this file whenever schema changes.

---

## Entity Relationship Diagram

```
categories [id]
   1
   |
   M
books [id, title, author, price, category_id]
   1
   |
   M
borrow_tickets [id, book_id, borrower_id, status]
   M
   |
   1
borrowers [id, full_name, email, phone]
```

---

## Tables

### categories

| Column | Type       | Constraints          | Description |
|--------|------------|----------------------|-------------|
| id     |   BIGINT   | PK, GENERATED AUTO   |             |
| name   |VARCHAR(100)| NOT NULL             | Category Name|

### books

| Column      | Type       | Constraints             | Description |
|-------------|------------|-------------------------|-------------|
| id          | BIGINT     | PK, GENERATED AUTO      |             |
| title       | VARCHAR(255)| NOT NULL                | Book title  |
| author      | VARCHAR(255)| NOT NULL                | Book author |
| price       | DECIMAL    | NOT NULL                | Book price  |
| category_id | BIGINT     | FK → categories(id)     | Belongs to  |


### borrowers

| Column    | Type       | Constraints             | Description |
|-----------|------------|-------------------------|-------------|
| id        | BIGINT     | PK, GENERATED AUTO      |             |
| full_name | VARCHAR(100)| NOT NULL                | Full Name   |
| email     | VARCHAR(255)| NOT NULL, UNIQUE        | Email addr  |
| phone     | VARCHAR(20)| NOT NULL                | Phone number|


### borrow_tickets

| Column      | Type        | Constraints             | Description |
|-------------|-------------|-------------------------|-------------|
| id          | BIGINT      | PK, GENERATED AUTO      |             |
| book_id     | BIGINT      | FK → books(id)          | Book tied to|
| borrower_id | BIGINT      | FK → borrowers(id)      | User tied to|
| borrow_date | TIMESTAMP   | NOT NULL                | Default Now |
| return_date | TIMESTAMP   | NULLABLE                | When returned|
| status      | VARCHAR(20) | NOT NULL                | Enum: BORROWED/RETURNED |

---

## Relationships Summary

| Relationship | Type | Owner Side | Join Config |
|-------------|------|-----------|-------------|
| Book → Category | ManyToOne | Book | `@JoinColumn(name = "category_id")` |
| BorrowTicket → Book | ManyToOne | BorrowTicket | `@JoinColumn(name = "book_id")` |
| BorrowTicket → Borrower | ManyToOne | BorrowTicket | `@JoinColumn(name = "borrower_id")` |

---

## Sample Data (Fake initialized mapping)

### Categories
| id | name |
|----|------|
| 1 | IT / Tech |
| 2 | Novel |

### Borrowers
| id | full_name | email | phone |
|----|-----------|-------|-------|
| 1 | Nguyen Van A | a@example.com | 0901234567 |
| 2 | Tran Thi B   | b@example.com | 0987654321 |
