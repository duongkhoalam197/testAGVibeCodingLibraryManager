package com.example.testaglibrarymanager.feature.book;

import com.example.testaglibrarymanager.dto.ApiResponse;
import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.feature.book.dto.BookDto;
import com.example.testaglibrarymanager.feature.book.dto.BookRequest;
import com.example.testaglibrarymanager.feature.book.dto.BookResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    public BookController(BookService bookService, BookMapper bookMapper) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        ServiceResult<List<BookDto>> result = bookService.getAllBooks(categoryId);
        List<BookResponse> responses = result.data().stream()
                .map(bookMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable("id") Long id) {
        ServiceResult<BookDto> result = bookService.getBookById(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(bookMapper.toResponse(result.data())));
        }
        return processError(result);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody @Valid BookRequest request) {
        ServiceResult<BookDto> result = bookService.createBook(request);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.created(bookMapper.toResponse(result.data())));
        }
        return processError(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable("id") Long id, 
                                                                @RequestBody @Valid BookRequest request) {
        ServiceResult<BookDto> result = bookService.updateBook(id, request);
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(bookMapper.toResponse(result.data())));
        }
        return processError(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") Long id) {
        ServiceResult<Void> result = bookService.deleteBook(id);
        if (result.isSuccess()) {
            return ResponseEntity.ok(ApiResponse.success(null));
        }
        return processError(result);
    }

    private <T, R> ResponseEntity<ApiResponse<R>> processError(ServiceResult<T> result) {
        HttpStatus status = switch (result.errorCode()) {
            case BOOK_NOT_FOUND, CATEGORY_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case BOOK_BEING_BORROWED_CANNOT_DELETE -> HttpStatus.CONFLICT;
            default -> HttpStatus.BAD_REQUEST;
        };
        return ResponseEntity.status(status)
                .body(ApiResponse.error(status.value(), result.errorCode().name(), result.message()));
    }
}
