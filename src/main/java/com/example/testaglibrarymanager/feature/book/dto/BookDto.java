package com.example.testaglibrarymanager.feature.book.dto;

import java.math.BigDecimal;

public record BookDto(
        Long id,
        String title,
        String author,
        BigDecimal price,
        Long categoryId,
        String categoryName
) {}
