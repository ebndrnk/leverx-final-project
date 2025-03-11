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
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private final static String DEFAULT_ERROR_MESSAGE = "SERVER_ERROR";


    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleGameNotFoundException(
            GameNotFoundException ex, HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SellerFromCommentNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleSellerFromCommentNotFoundException(
            SellerFromCommentNotFoundException ex, HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserWithThisEmailNotDoesntChangePasswordException.class)
    public ResponseEntity<ErrorInfo> handleUserWithThisEmailNotDoesntChangedPasswordException(
            UserWithThisEmailNotDoesntChangePasswordException ex, HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleProfileNotFoundException(
            ProfileNotFoundException ex, HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorInfo> handleNoResourceFoundException(
            NoResourceFoundException ex, HttpServletRequest request) {
        log.error("NoResourceFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoAuthorityForActionException.class)
    public ResponseEntity<ErrorInfo> handleNoAuthorityForActionException(
            NoAuthorityForActionException ex, HttpServletRequest request) {
        log.error("NoAuthorityForActionException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleCommentNotFoundException(
            CommentNotFoundException ex, HttpServletRequest request) {
        log.error("CommentNotFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentAuthorNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleCommentAuthorNotFoundException(
            CommentAuthorNotFoundException ex, HttpServletRequest request) {
        log.error("CommentAuthorNotFoundException: {}", ex.getMessage(), ex);
        ErrorInfo errorInfo = new ErrorInfo(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }

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
