package com.example.testaglibrarymanager.model.dto;

import java.time.LocalDateTime;

public record BorrowerDto(
                String eventType,
                Long id,
                String fullName,
                String email,
                String phone,
                LocalDateTime eventTimestamp) {
}
