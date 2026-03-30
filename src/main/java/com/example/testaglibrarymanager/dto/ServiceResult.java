package com.example.testaglibrarymanager.dto;

import com.example.testaglibrarymanager.exception.ErrorCode;

public record ServiceResult<T>(
        boolean isSuccess,
        ErrorCode errorCode,
        String message,
        T data
) {
    public static <T> ServiceResult<T> success(T data) {
        return new ServiceResult<>(true, ErrorCode.SUCCESS, "Thành công", data);
    }

    public static <T> ServiceResult<T> fail(ErrorCode errorCode, String message) {
        return new ServiceResult<>(false, errorCode, message, null);
    }
}
