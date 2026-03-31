package com.example.testaglibrarymanager.feature.borrower.dto;

public record BorrowerDto(
        Long id,
        String fullName,
        String email,
        String phone
) {
}
