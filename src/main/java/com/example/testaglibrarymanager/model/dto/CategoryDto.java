package com.example.testaglibrarymanager.model.dto;

import java.time.LocalDateTime;

public record CategoryDto(
                String eventType,
                Long id,
                String name,
                LocalDateTime eventTimestamp) {
}
