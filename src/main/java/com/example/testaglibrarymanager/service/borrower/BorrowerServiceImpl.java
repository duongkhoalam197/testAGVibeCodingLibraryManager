package com.example.testaglibrarymanager.service.borrower;

import com.example.testaglibrarymanager.model.dto.ServiceResult;
import com.example.testaglibrarymanager.model.request.UpdateBorrowerRequest;
import com.example.testaglibrarymanager.model.entity.Borrower;
import com.example.testaglibrarymanager.config.KafkaConfig;
import com.example.testaglibrarymanager.mapper.BorrowerMapper;
import com.example.testaglibrarymanager.messaging.BorrowerEventProducer;
import com.example.testaglibrarymanager.repository.BorrowerRepository;
import com.example.testaglibrarymanager.model.dto.BorrowerDto;
import com.example.testaglibrarymanager.util.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

import com.example.testaglibrarymanager.model.request.CreateBorrowerRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowerServiceImpl implements BorrowerService {

    private final BorrowerRepository borrowerRepository;
    private final BorrowerMapper borrowerMapper;
    private final BorrowerEventProducer borrowerEventProducer;

    @Override
    @Transactional
    public ServiceResult<BorrowerDto> createBorrower(CreateBorrowerRequest request) {
        if (borrowerRepository.existsByEmail(request.email())) {
            return ServiceResult.fail(ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS,
                    "Email đã được sử dụng: " + request.email());
        }

        Borrower borrower = borrowerMapper.toEntity(request);
        Borrower savedBorrower = borrowerRepository.save(borrower);
        
        // Bắn event Kafka sử dụng Mapper mới
        BorrowerDto eventDto = borrowerMapper.toEventDto(savedBorrower, KafkaConfig.BORROWER_CREATE_TOPIC);
        borrowerEventProducer.publishBorrowerEvent(KafkaConfig.BORROWER_CREATE_TOPIC, eventDto);
        
        return ServiceResult.success(borrowerMapper.toDto(savedBorrower));
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceResult<BorrowerDto> getBorrowerById(Long id) {
        return borrowerRepository.findById(id)
                .map(borrower -> ServiceResult.success(borrowerMapper.toDto(borrower)))
                .orElseGet(() -> ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND,
                        "Không tìm thấy người mượn với id: " + id));
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
                return ServiceResult.<BorrowerDto>fail(ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS,
                        "Email đã được sử dụng: " + request.email());
            }

            borrower.setFullName(request.fullName());
            borrower.setEmail(request.email());
            borrower.setPhone(request.phone());
            Borrower updatedBorrower = borrowerRepository.save(borrower);

            // Bắn event Kafka Update
            BorrowerDto eventDto = borrowerMapper.toEventDto(updatedBorrower, KafkaConfig.BORROWER_UPDATE_TOPIC);
            borrowerEventProducer.publishBorrowerEvent(KafkaConfig.BORROWER_UPDATE_TOPIC, eventDto);

            return ServiceResult.success(borrowerMapper.toDto(updatedBorrower));
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với id: " + id));
    }

    @Override
    @Transactional
    public ServiceResult<Void> deleteBorrower(Long id) {
        return borrowerRepository.findById(id).map(borrower -> {
            // Lưu lại DTO trước khi xóa để bắn event
            BorrowerDto eventDto = borrowerMapper.toEventDto(borrower, KafkaConfig.BORROWER_DELETE_TOPIC);
            
            borrowerRepository.delete(borrower);
            
            // Bắn event Kafka Delete
            borrowerEventProducer.publishBorrowerEvent(KafkaConfig.BORROWER_DELETE_TOPIC, eventDto);
            
            return ServiceResult.<Void>success(null);
        }).orElseGet(() -> ServiceResult.fail(ErrorCode.BORROWER_NOT_FOUND, "Không tìm thấy người mượn với id: " + id));
    }
}
