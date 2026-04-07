# Project Status
> Date: 2026-04-07 | Author: Antigravity | Session: 5

## Current Progress

### Completed
- [2026-04-07] **BorrowTicket Module**: Hoàn tất logic mượn sách, tối ưu trình tự lưu DB rồ mới bắn Kafka (Transactional Consistency).
- [2026-04-07] **Infrastructure**: Tích hợp Kafka thành công cho BorrowTicket, sửa lỗi chính tả tại Producer/Consumer.
- [2026-04-07] **Category Module**: Tối ưu hóa logic Service (xóa 1 lần DB), sạch lỗi chính tả và dọn dẹp Mapper.
- [2026-04-07] **Fix**: Sửa lỗi khai báo package trong `TestAgLibraryManagerApplicationTests`.
- [2026-04-06] **Infrastructure**: Integrated Kafka (KRaft mode) using Docker Compose and Spring Boot Kafka Starter.
- [2026-03-31] **Refactor**: Rewrote `Category` and `Borrower` components to use `ServiceResult` Pattern and SOLID Mappers.
- [2026-03-31] **Testing**: Implemented 100% Unit Test coverage for `CategoryServiceImpl` and `BorrowerServiceImpl`.
- [2026-03-31] **Major Architecture Upgrade**: Transitioned source code from Feature-based (`feature/*`) to **Layered Architecture** (`model/`, `controller/`, `service/`, `repository/`, `mapper/`).
- [2026-03-31] **Documentation**: "Context Anchoring" (Ký Sinh Context). Distributed `CONTEXT.md` files directly into their respective `service/{feature}/` brains to prevent document rot.
- [2026-03-31] **Workflows**: Enforced Context Physical Anchors in `/review-pr.md` as a Blocker to ensure documentation never points to non-existent classes.
- [2026-03-31] Workspace: Successfully purged all temporary Java scripts and refactoring tools.

## In Progress
- [ ] Integration Testing with `MockMvc` for all REST endpoints (`@SpringBootTest`).
- [ ] Implement `BookService` and `BorrowTicketService` Unit Tests.

## Next Tasks
1. Add Integration Tests for `BookController`, `CategoryController`, `BorrowerController`, and `BorrowTicketController`.
2. Write Unit tests for `BookService`.
3. Refine automated integration test flows using `application-test.yml` and test containers or H2 database.
4. Implement JWT Authentication / User Registration (if requested by Product Owner).

## Warnings
- ⚠️ Compilation on Spring Boot 4/Spring 7 requires explicit parameter names or `-parameters` flag.
- ⚠️ Do NOT use `fromEntity` in DTOs. Always use the `mapper/` layer components for mapping logic.
- ⚠️ **Vibe Coding Law**: `CONTEXT.md` MUST reside inside the `service/{feature}/` directory and contain the "Hệ Thống Mỏ Neo Vật Lý" pointing to Entity, Controller, and Service. If Anchor Links break, PR is denied.

## Deferred Issues
- [P2] N+1 Query potential in `BookServiceImpl.getAllBooks`.