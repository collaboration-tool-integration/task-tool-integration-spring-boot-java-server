package com.pbl.tasktoolintegration.monday.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum BaseExceptionStatus {
    MONDAY_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Monday server error"),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "Board not found"),
    CONFIG_NOT_FOUND(HttpStatus.NOT_FOUND, "Config not found"),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error"),;
    private final HttpStatus status;
    private final String message;

    private BaseExceptionStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}