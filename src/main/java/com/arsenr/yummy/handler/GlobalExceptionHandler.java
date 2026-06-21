package com.arsenr.yummy.handler;

import com.arsenr.yummy.exception.TokenException;
import com.arsenr.yummy.exception.UserRegistrationException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(ValidationException.class)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("Validation Exception: ", e);
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError) -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });
        return ErrorResponse.builder()
                .error(MethodArgumentNotValidException.class.getSimpleName())
                .message(e.getMessage())
                .details(errors)
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .path(e.getNestedPath())
                .build();
    }

    @ExceptionHandler(UserRegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserRegistrationException(UserRegistrationException e) {
        log.warn("User Exist Exception: ", e);
        return ErrorResponse.builder()
                .error(UserRegistrationException.class.getSimpleName())
                .message(e.getMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("User Not Found Exception: ", e);
        return ErrorResponse.builder()
                .error(UsernameNotFoundException.class.getSimpleName())
                .message(e.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleTokenException(TokenException e) {
        log.warn("Token Exception: ", e);
        return ErrorResponse.builder()
                .error(TokenException.class.getSimpleName())
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(Instant.now())
                .build();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ErrorResponse handleExpiredJwtException(ExpiredJwtException e) {
        log.warn("Expired JWT Exception: ", e);
        return ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleIllegalArgument(IllegalArgumentException e) {
        log.warn("Illegal Argument Exception: ", e);
        return ErrorResponse.of(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                e.getMessage()
        );
    }

    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleMalformedJwtException(MalformedJwtException e) {
        log.warn("Malformed JWT Exception: ", e);
        return ErrorResponse.of(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                e.getMessage()
        );
    }
    //PSQLException
}
