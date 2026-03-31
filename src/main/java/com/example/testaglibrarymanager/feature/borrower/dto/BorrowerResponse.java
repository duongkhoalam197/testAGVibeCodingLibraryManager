package com.example.testaglibrarymanager.feature.borrower.dto;

public record BorrowerResponse(
        Long id,
        String fullName,
        String email,
        String phone
) {
}
