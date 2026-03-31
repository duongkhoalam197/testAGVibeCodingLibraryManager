package com.example.testaglibrarymanager.service.book;

import com.example.testaglibrarymanager.model.request.BookRequest;
import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.dto.BookDto;



import java.util.List;

public interface BookService {
    ServiceResult<List<BookDto>> getAllBooks(Long categoryId);
    ServiceResult<BookDto> getBookById(Long id);
    ServiceResult<BookDto> createBook(BookRequest request);
    ServiceResult<BookDto> updateBook(Long id, BookRequest request);
    ServiceResult<Void> deleteBook(Long id);
}

