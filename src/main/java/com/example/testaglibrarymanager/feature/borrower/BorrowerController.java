package com.example.testaglibrarymanager.feature.borrower;

import com.example.testaglibrarymanager.dto.ApiResponse;
import com.example.testaglibrarymanager.dto.ServiceResult;
import com.example.testaglibrarymanager.exception.ErrorCode;
import com.example.testaglibrarymanager.feature.borrower.dto.BorrowerDto;
import com.example.testaglibrarymanager.feature.borrower.dto.BorrowerResponse;
import com.example.testaglibrarymanager.feature.borrower.dto.CreateBorrowerRequest;
import com.example.testaglibrarymanager.feature.borrower.dto.UpdateBorrowerRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrowers")
public class BorrowerController {

    private final BorrowerService borrowerService;
    private final BorrowerMapper borrowerMapper;

    public BorrowerController(BorrowerService borrowerService, BorrowerMapper borrowerMapper) {
        this.borrowerService = borrowerService;
        this.borrowerMapper = borrowerMapper;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowerResponse>>> getAllBorrowers() {
        ServiceResult<List<BorrowerDto>> result = borrowerService.getAllBorrowers();
        
        List<BorrowerResponse> body = result.data().stream()
                .map(borrowerMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(body));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowerResponse>> getBorrowerById(@PathVariable("id") Long id) {
        ServiceResult<BorrowerDto> result = borrowerService.getBorrowerById(id);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        BorrowerResponse response = borrowerMapper.toResponse(result.data());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BorrowerResponse>> createBorrower(@Valid @RequestBody CreateBorrowerRequest request) {
        ServiceResult<BorrowerDto> result = borrowerService.createBorrower(request);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        BorrowerResponse response = borrowerMapper.toResponse(result.data());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created(response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BorrowerResponse>> updateBorrower(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateBorrowerRequest request) {

        ServiceResult<BorrowerDto> result = borrowerService.updateBorrower(id, request);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        BorrowerResponse response = borrowerMapper.toResponse(result.data());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBorrower(@PathVariable("id") Long id) {
        ServiceResult<Void> result = borrowerService.deleteBorrower(id);

        if (!result.isSuccess()) {
            return mapError(result);
        }

        return ResponseEntity.ok(ApiResponse.success(null));
    }

    private <T> ResponseEntity<ApiResponse<T>> mapError(ServiceResult<?> result) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        if (result.errorCode() == ErrorCode.BORROWER_NOT_FOUND) {
            status = HttpStatus.NOT_FOUND;
        } else if (result.errorCode() == ErrorCode.BORROWER_EMAIL_ALREADY_EXISTS) {
            status = HttpStatus.CONFLICT;
        }

        return ResponseEntity.status(status).body(ApiResponse.error(status.value(), result.errorCode().name(), result.message()));
    }
}
