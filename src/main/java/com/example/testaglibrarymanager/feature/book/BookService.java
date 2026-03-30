package com.example.testaglibrarymanager.feature.book;

import com.example.testaglibrarymanager.feature.book.dto.BookRequest;
import com.example.testaglibrarymanager.feature.book.dto.BookResponse;

import java.util.List;

public interface BookService {
    List<BookResponse> getAllBooks(Long categoryId);
    BookResponse getBookById(Long id);
    BookResponse createBook(BookRequest request);
    BookResponse updateBook(Long id, BookRequest request);
    void deleteBook(Long id);
}
