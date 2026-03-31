package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.dto.CategoryDto;
import com.example.testaglibrarymanager.model.response.BookResponse;
import com.example.testaglibrarymanager.model.entity.Book;
import com.example.testaglibrarymanager.model.dto.BookDto;
import com.example.testaglibrarymanager.model.entity.Category;


import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookDto toDto(Book book) {
        if (book == null) return null;
        
        Long categoryId = book.getCategory() != null ? book.getCategory().getId() : null;
        String categoryName = book.getCategory() != null ? book.getCategory().getName() : null;
        
        return new BookDto(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                categoryId,
                categoryName
        );
    }

    public BookResponse toResponse(BookDto dto) {
        if (dto == null) return null;
        
        BookResponse.CategoryDto categoryResponse = dto.categoryId() != null 
                ? new BookResponse.CategoryDto(dto.categoryId(), dto.categoryName())
                : null;
                
        return new BookResponse(
                dto.id(),
                dto.title(),
                dto.author(),
                dto.price(),
                categoryResponse
        );
    }
}

