package com.example.testaglibrarymanager.feature.borrower;

import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.feature.borrower.dto.BorrowerDto;
import com.example.testaglibrarymanager.feature.borrower.dto.CreateBorrowerRequest;
import com.example.testaglibrarymanager.feature.borrower.dto.UpdateBorrowerRequest;

import java.util.List;

public interface BorrowerService {
    ServiceResult<BorrowerDto> createBorrower(CreateBorrowerRequest request);
    ServiceResult<BorrowerDto> getBorrowerById(Long id);
    ServiceResult<List<BorrowerDto>> getAllBorrowers();
    ServiceResult<BorrowerDto> updateBorrower(Long id, UpdateBorrowerRequest request);
    ServiceResult<Void> deleteBorrower(Long id);
}
