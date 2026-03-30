package com.example.testaglibrarymanager.exception;

public enum ErrorCode {
    SUCCESS,
    
    // System & Infrastructure Errors (Unforeseeable or Global)
    UNCATEGORIZED_EXCEPTION,
    INVALID_JSON_REQUEST,
    VALIDATION_FAILED,
    MISSING_PARAMETER,
    METHOD_NOT_ALLOWED,
    URL_NOT_FOUND,
    DATABASE_CONSTRAINT_VIOLATION,

    // Domain: Book
    BOOK_NOT_FOUND,
    BOOK_ALREADY_BORROWED,
    BOOK_BEING_BORROWED_CANNOT_DELETE,

    // Domain: Borrower
    BORROWER_NOT_FOUND,
    
    // Domain: Category
    CATEGORY_NOT_FOUND,

    // Generic Business Fallback
    BUSINESS_ERROR
}
