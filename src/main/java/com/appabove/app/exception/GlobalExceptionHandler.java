package com.appabove.app.exception;

import com.appabove.app.dto.response.BaseResponse;
import com.appabove.app.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageService messageService;

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<BaseResponse<Void>> handleRuntimeException(RuntimeException ex) {
        log.error("Unhandled exception occurred", ex);
        BaseResponse<Void> response = BaseResponse.error(ex.getMessage());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Void>> handleException(Exception ex) {
        log.error("Unhandled exception occurred", ex);
        BaseResponse<Void> response = new BaseResponse<>(500, messageService.get("internal.server.error"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}