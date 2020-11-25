package com.kil.tutor.controller;

import com.kil.tutor.entity.UnhandledException;
import com.kil.tutor.repository.UnhandledExceptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;

@ControllerAdvice
public class DbExceptionHandler {
    private final UnhandledExceptionRepository exceptionRepository;

    @Autowired
    public DbExceptionHandler(UnhandledExceptionRepository exceptionRepository) {
        this.exceptionRepository = exceptionRepository;
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> handleException(Exception exception) {
        UnhandledException unhandledException = UnhandledException.builder()
                .message(exception.getMessage())
                .stackTrace(Arrays.toString(exception.getStackTrace()))
                .build();
        UnhandledException savedException = exceptionRepository.save(unhandledException);
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("errorId: " + savedException.getId());
    }
}
