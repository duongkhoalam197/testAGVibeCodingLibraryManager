package com.example.testaglibrarymanager.feature.book;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.feature.book.dto.BookDto;
import com.example.testaglibrarymanager.feature.book.dto.BookRequest;

import java.util.List;

public interface BookService {
    ServiceResult<List<BookDto>> getAllBooks(Long categoryId);
    ServiceResult<BookDto> getBookById(Long id);
    ServiceResult<BookDto> createBook(BookRequest request);
    ServiceResult<BookDto> updateBook(Long id, BookRequest request);
    ServiceResult<Void> deleteBook(Long id);
}
