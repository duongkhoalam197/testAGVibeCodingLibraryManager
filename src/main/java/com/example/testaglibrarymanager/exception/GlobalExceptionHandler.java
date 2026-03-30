package com.example.testaglibrarymanager.exception;

import com.example.testaglibrarymanager.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Group 1: Lỗi Validation dữ liệu đầu vào (@Valid)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (a, b) -> a));
                        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(
                        HttpStatus.BAD_REQUEST.value(),
                        ErrorCode.VALIDATION_FAILED.name(),
                        errors,
                        "Dữ liệu đầu vào không hợp lệ",
                        LocalDateTime.now()
                ));
    }
    
    /**
     * Group 2: Lỗi tham số URL/JSON không hợp lệ (Sai kiểu, rác JSON)
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ResponseEntity<ApiResponse<Void>> handleFormatError(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST.value(),
                        ErrorCode.INVALID_JSON_REQUEST.name(),
                        "Định dạng yêu cầu không hợp lệ (Sai kiểu dữ liệu hoặc JSON lỗi)"
                ));
    }

    /**
     * Group 3: Lỗi thiếu tham số bắt buộc (Vd: thiếu ?id=...)
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(
                        HttpStatus.BAD_REQUEST.value(),
                        ErrorCode.MISSING_PARAMETER.name(),
                        "Thiếu tham số bắt buộc: " + ex.getParameterName()
                ));
    }

    /**
     * Group 4: Lỗi Sai URL (404 API Not Found)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(
                        HttpStatus.NOT_FOUND.value(),
                        ErrorCode.URL_NOT_FOUND.name(),
                        "Đường dẫn API không tồn tại"
                ));
    }

    /**
     * Group 5: Lỗi Sai Method (GET nhưng gọi POST...)
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(
                        HttpStatus.METHOD_NOT_ALLOWED.value(),
                        ErrorCode.METHOD_NOT_ALLOWED.name(),
                        "Phương thức HTTP '" + ex.getMethod() + "' không được hỗ trợ cho endpoint này"
                ));
    }

    /**
     * Group 6: Lỗi Ràng buộc Database dự phòng (Unique/FK)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDbIntegrity(DataIntegrityViolationException ex) {
        log.warn("Database Integrity Violation: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(
                        HttpStatus.CONFLICT.value(),
                        ErrorCode.DATABASE_CONSTRAINT_VIOLATION.name(),
                        "Vi phạm ràng buộc dữ liệu (Dữ liệu đã tồn tại hoặc đang được tham chiếu)"
                ));
    }

    /**
     * Group 7: Lỗi KHÔNG Lường Trước (Fall-safe)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneral(Exception ex) {
        log.error("Hệ thống xảy ra lỗi không lường trước: ", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        ErrorCode.UNCATEGORIZED_EXCEPTION.name(),
                        "Lỗi hệ thống không xác định (Vui lòng liên hệ quản trị viên)"
                ));
    }
}
