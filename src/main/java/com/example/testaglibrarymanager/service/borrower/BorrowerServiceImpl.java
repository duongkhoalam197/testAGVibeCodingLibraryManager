package com.example.testaglibrarymanager.service.borrower;

import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.request.UpdateBorrowerRequest;
import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.mapper.BorrowerMapper;
import com.example.testaglibrarymanager.repository.BorrowerRepository;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;
import com.example.testaglibrarymanager.util.exception.ErrorCode;
import com.example.testaglibrarymanager.model.request.CreateBorrowerRequest;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BorrowerMapper borrowerMapper;

    public BorrowerServiceImpl(BorrowerRepository borrowerRepository, BorrowerMapper borrowerMapper) {
        this.borrowerRepository = borrowerRepository;
        this.borrowerMapper = borrowerMapper;
    }

    @Override
    @Transactional
    public ServiceResult<BorrowerDto> createBorrower(CreateBorrowerRequest request) {
        if (borrowerRepository.existsByEmail(request.email())) {
            return ServiceResult.fail(ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS, "Email đã được sử dụng: " + request.email());
        }

        Borrower borrower = borrowerMapper.toEntity(request);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        return ServiceResult.success(borrowerMapper.toDto(savedBorrower));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<BorrowerDto> getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(borrower -> ServiceResult.success(borrowerMapper.toDto(borrower)))
                .orElseGet(() -> ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<List<BorrowerDto>> getAllBorrowers() {
        List<BorrowerDto> borrowers = borrowerRepository.findAll().stream()
                .map(borrowerMapper::toDto)
                .collect(Collectors.toList());
        return ServiceResult.success(borrowers);
    }

    @Override
    @Transactional
    public ServiceResult<BorrowerDto> updateBorrower(Long id, UpdateBorrowerRequest request) {
        return borrowerRepository.findById(id).map(borrower -> {
            // Check email collision
            if (!borrower.getEmail().equals(request.email()) && borrowerRepository.existsByEmail(request.email())) {
                return ServiceResult.<BorrowerDto>fail(ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS, "Email đã được sử dụng: " + request.email());
            }

            borrower.setFullName(request.fullName());
            borrower.setEmail(request.email());
            borrower.setPhone(request.phone());
            Borrower updatedBorrower = borrowerRepository.save(borrower);
            return ServiceResult.success(borrowerMapper.toDto(updatedBorrower));
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với id: " + id));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteBorrower(Long id) {
        if (!borrowerRepository.existsById(id)) {
            return ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với id: " + id);
        }
        borrowerRepository.deleteById(id);
        return ServiceResult.success(null);
    }
}

