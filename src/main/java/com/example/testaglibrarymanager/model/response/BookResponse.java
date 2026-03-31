package com.example.testaglibrarymanager.model.response;

import com.example.testaglibrarymanager.model.dto.CategoryDto;


import java.math.BigDecimal;

public record BookResponse(
        Long id,
        String title,
        String author,
        BigDecimal price,
        CategoryDto category
) {
    public record CategoryDto(Long id, String name) {}
}

