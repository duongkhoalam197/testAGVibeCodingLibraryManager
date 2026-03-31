package com.example.testaglibrarymanager.service.borrower;

import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.request.UpdateBorrowerRequest;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;
import com.example.testaglibrarymanager.model.request.CreateBorrowerRequest;



import java.util.List;

public interface BorrowerService {
    ServiceResult<BorrowerDto> createBorrower(CreateBorrowerRequest request);
    ServiceResult<BorrowerDto> getBorrowerById(Long id);
    ServiceResult<List<BorrowerDto>> getAllBorrowers();
    ServiceResult<BorrowerDto> updateBorrower(Long id, UpdateBorrowerRequest request);
    ServiceResult<Void> deleteBorrower(Long id);
}

