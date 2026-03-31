package com.example.testaglibrarymanager.mapper;

import com.example.testaglibrarymanager.model.response.BorrowerResponse;
import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;
import com.example.testaglibrarymanager.model.request.CreateBorrowerRequest;


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
                entity.getId(),
                entity.getFullName(),
                entity.getEmail(),
                entity.getPhone()
        );
    }

    public BorrowerResponse toResponse(BorrowerDto dto) {
        if (dto == null) {
            return null;
        }
        return new BorrowerResponse(
                dto.id(),
                dto.fullName(),
                dto.email(),
                dto.phone()
        );
    }
}

