package com.example.testaglibrarymanager.model.dto;



public record BorrowerDto(
        Long id,
        String fullName,
        String email,
        String phone
) {
}

