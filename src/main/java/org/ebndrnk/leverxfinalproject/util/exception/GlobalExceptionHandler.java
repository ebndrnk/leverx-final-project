package org.ebndrnk.leverxfinalproject.util.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.ebndrnk.leverxfinalproject.util.exception.dto.EmailAlreadyExistsException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UserNotFoundException;
import org.ebndrnk.leverxfinalproject.util.exception.dto.UsernameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static String DEFAULT_ERROR_MESSAGE = "SERVER_ERROR";

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorInfo> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Bad credentials",
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorInfo> handleEmailAlreadyExistsException(
            EmailAlreadyExistsException ex, HttpServletRequest request) {

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
            EmailAlreadyExistsException ex, HttpServletRequest request) {

        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return new ResponseEntity<>(errorInfo, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleGlobalException(
            Exception ex, HttpServletRequest request) {
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
