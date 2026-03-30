package com.example.testaglibrarymanager.feature.book.dto;

import com.example.testaglibrarymanager.feature.book.Book;

import java.math.BigDecimal;

public record BookResponse(
        Long id,
        String title,
        String author,
        BigDecimal price,
        CategoryDto category
) {

    public record CategoryDto(Long id, String name) {}

    public static BookResponse fromEntity(Book book) {
        CategoryDto categoryDto = book.getCategory() != null 
                ? new CategoryDto(book.getCategory().getId(), book.getCategory().getName())
                : null;

        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                categoryDto
        );
    }
}
