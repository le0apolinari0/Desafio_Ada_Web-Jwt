package br.com.ada.ConsoleApp.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorType errorType;
    private final String details;

    public AppException(ErrorType errorType, String message, String details) {
        super(message);
        this.errorType = errorType;
        this.details = details;
    }

    public AppException(ErrorType errorType, String message, String details, Throwable cause) {
        super(message, cause);
        this.errorType = errorType;
        this.details = details;
    }
}


