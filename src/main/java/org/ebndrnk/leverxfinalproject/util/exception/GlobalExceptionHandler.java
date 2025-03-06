package org.ebndrnk.leverxfinalproject.util.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.ebndrnk.leverxfinalproject.util.exception.dto.*;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static String DEFAULT_ERROR_MESSAGE = "SERVER_ERROR";

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorInfo> handleNoSuchElementException(
            NoSuchElementException ex, HttpServletRequest request) {
        log.error("NoSuchElementException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotConfirmedException.class)
    public ResponseEntity<ErrorInfo> handleUnauthorizedException(
            NotConfirmedException ex, HttpServletRequest request) {
        log.error("NotConfirmedException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorInfo> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {
        log.error("BadCredentialsException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, HttpServletRequest request) {
        log.error("EmailAlreadyExistsException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleUserNotFoundException(
            UserNotFoundException ex, HttpServletRequest request) {
        log.error("UserNotFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleUsernameAlreadyExistsException(
            UsernameAlreadyExistsException ex, HttpServletRequest request) {
        log.error("UsernameAlreadyExistsException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessages = ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
        log.error("MethodArgumentNotValidException: {}", errorMessages, ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errorMessages,
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGlobalException(
            Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                DEFAULT_ERROR_MESSAGE,
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
