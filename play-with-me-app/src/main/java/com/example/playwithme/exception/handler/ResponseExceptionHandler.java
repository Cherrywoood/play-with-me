package com.example.playwithme.exception.handler;

import com.example.playwithme.exception.EntityExistsException;
import com.example.playwithme.exception.EntityNotFoundException;
import com.example.playwithme.exception.ForbiddenException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
public class ResponseExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        List<Violation> violations = ex.getConstraintViolations()
                .stream()
                .map(violation ->
                        new Violation(violation.getPropertyPath().toString(), violation.getMessage())
                )
                .collect(Collectors.toList());

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "invalid data",
                request.getDescription(false),
                violations
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        List<Violation> violations = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        new Violation(fieldError.getField(), fieldError.getDefaultMessage())
                )
                .collect(Collectors.toList());

        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                "invalid data",
                request.getDescription(false),
                violations
        );
    }

    @ExceptionHandler(EntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleEntityExistsException(EntityExistsException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorMessage handleIllegalArgumentException(ForbiddenException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false)
        );
    }

}
