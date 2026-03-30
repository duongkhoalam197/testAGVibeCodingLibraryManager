package com.example.testaglibrarymanager.feature.book;

import com.example.testaglibrarymanager.dto.ApiResponse;
import com.example.testaglibrarymanager.feature.book.dto.BookRequest;
import com.example.testaglibrarymanager.feature.book.dto.BookResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(
            @RequestParam(name = "categoryId", required = false) Long categoryId) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getAllBooks(categoryId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.success(bookService.getBookById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@RequestBody @Valid BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(bookService.createBook(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@PathVariable("id") Long id, 
                                                                @RequestBody @Valid BookRequest request) {
        return ResponseEntity.ok(ApiResponse.success(bookService.updateBook(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
