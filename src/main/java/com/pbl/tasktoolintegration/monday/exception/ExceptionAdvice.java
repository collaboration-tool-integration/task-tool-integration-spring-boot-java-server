package com.pbl.tasktoolintegration.monday.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    // 직접 throw한 BaseException을 처리하는 핸들러
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseExceptionRes> handleBaseException(BaseException exception) {
        BaseExceptionRes response = BaseExceptionRes.builder()
            .message(exception.getStatus().getMessage())
            .build();
        return new ResponseEntity<>(response, exception.getStatus().getStatus());
    }

    // 그 외 모든 예외를 처리하는 핸들러
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseExceptionRes> handleGeneralException(Exception exception) {
        BaseExceptionRes response = BaseExceptionRes.builder()
            .message(exception.getMessage())
            .build();

        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}