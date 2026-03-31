package com.example.testaglibrarymanager.model.response;



public record BorrowerResponse(
        Long id,
        String fullName,
        String email,
        String phone
) {
}

