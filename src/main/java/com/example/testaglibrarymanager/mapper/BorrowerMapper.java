package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.response.BorrowerResponse;
import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;
import com.example.testaglibrarymanager.model.request.CreateBorrowerRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class BorrowerMapper {

    public Borrower toEntity(CreateBorrowerRequest request) {
        if (request == null) {
            return null;
        }
        return new Borrower(request.fullName(), request.email(), request.phone());
    }

    public BorrowerDto toDto(Borrower entity) {
        if (entity == null) {
            return null;
        }
        return new BorrowerDto(
                null,
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                null);
    }

    public BorrowerDto toEventDto(Borrower entity, String eventType) {
        if (entity == null) {
            return null;
        }
        return new BorrowerDto(
                eventType,
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone(),
                LocalDateTime.now());
    }

    public BorrowerResponse toResponse(BorrowerDto dto) {
        if (dto == null) {
            return null;
        }
        return new BorrowerResponse(
                dto.id(),
                dto.fullName(),
                dto.email(),
                dto.phone());
    }
}
