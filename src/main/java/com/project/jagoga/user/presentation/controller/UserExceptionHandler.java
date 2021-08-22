package com.project.jagoga.user.presentation.controller;

import com.project.jagoga.exception.dto.ApiResponse;
import com.project.jagoga.exception.user.DuplicatedUserException;
import com.project.jagoga.exception.user.NotFoundUserException;
import com.project.jagoga.exception.user.UserAuthenticationFailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = {"com.project.jagoga.user.presentation.controller"})
public class UserExceptionHandler {

    @ExceptionHandler(DuplicatedUserException.class)
    public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<ApiResponse<?>> handleNotFoundUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(UserAuthenticationFailException.class)
    public ResponseEntity<ApiResponse<?>> handleUserAuthenticationFailException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<?>> handleIllegalArgumentException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createError(exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleValidationExceptions(BindingResult bindingResult) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
    }

    // TODO : Json 응답 포맷 통일하기
    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<String> handleNotFoundUserException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(UserAuthenticationFailException.class)
    public ResponseEntity<String> handleUserAuthenticationFailException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}