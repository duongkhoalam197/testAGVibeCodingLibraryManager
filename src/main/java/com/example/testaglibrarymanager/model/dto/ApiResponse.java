package com.example.testaglibrarymanager.model.dto;

import com.example.testaglibrarymanager.util.exception.ErrorCode;


import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

public record ApiResponse<T>(
        int statusCode,
        String code,
        T data,
        String message,
        LocalDateTime timestamp
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), ErrorCode.SUCCESS.name(), data, "Thành công", LocalDateTime.now());
    }

    public static <T> ApiResponse<T> created(T data) {
        return new ApiResponse<>(HttpStatus.CREATED.value(), ErrorCode.SUCCESS.name(), data, "Tạo mới thành công", LocalDateTime.now());
    }

    public static <T> ApiResponse<T> error(int statusCode, String code, String message) {
        return new ApiResponse<>(statusCode, code, null, message, LocalDateTime.now());
    }
}

