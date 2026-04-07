package com.example.testaglibrarymanager.model.dto;

import java.time.LocalDateTime;

public record BookEventDto(
        String eventType,
        Long bookId,
        String title,
        String author,
        Long categoryId,
        LocalDateTime eventTimestamp) {
}
